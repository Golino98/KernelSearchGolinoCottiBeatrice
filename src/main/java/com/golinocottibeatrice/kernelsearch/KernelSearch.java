package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.bucket.Bucket;
import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.kernel.Kernel;
import com.golinocottibeatrice.kernelsearch.solver.*;
import gurobi.GRBException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementa il metodo della kernel search, usando GUROBI come risolutore.
 * I parametri di configurazione vengono letti da un'istanza di {@link SearchConfiguration}.
 */
public class KernelSearch {
    // Soglia tra il tempo trascorso e il tempo massimo di esecuzione
    // sotto il cui viene fermato il programma
    protected static final int TIME_THRESHOLD = 2;

    protected final SearchConfiguration config;
    protected final Logger log;
    protected final Solver solver;
    protected List<Bucket> buckets;
    protected Kernel kernel;

    protected final Instance instance;
    protected long startTime;
    // Variabili del problema
    protected List<Variable> variables;
    // Miglior soluzione trovata
    protected Solution bestSolution;

    /**
     * Crea una nuova istanza di kernel search.
     *
     * @param config La configurazione del metodo.
     */
    public KernelSearch(SearchConfiguration config) {
        this.config = config;
        instance = config.getInstance();
        log = config.getLogger();
        solver = config.getSolver();
    }

    /**
     * Avvia la kernel search.
     *
     * @throws GRBException Errore di Gurobi.
     */
    public SearchResult start() throws GRBException {
        startTime=System.nanoTime();
        log.start(instance.getName(), Instant.now());

        solveRelaxation();

        config.getVariableSorter().sort(variables);

        kernel = config.getKernelBuilder().build(variables, config);
        this.kernel.getVariables().forEach(variable -> variable.setFromBucket(false));

        // Nei bucket vanno solo le variabili che non sono già nel kernel
        buckets = config.getBucketBuilder().build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);

        this.solveKernel();
        this.iterateBuckets();
        log.end(bestSolution.getObjective(), elapsedTime());

        return new SearchResult(bestSolution, elapsedTime(), elapsedTime() + TIME_THRESHOLD >= config.getTimeLimit());
    }

    protected void solveRelaxation() throws GRBException {
        var model = solver.createModel(instance, config.getTimeLimit(), true);
        log.relaxStart();

        var solution = model.solve();
        log.solution(solution.getObjective(), elapsedTime());

        variables = solution.getVariables();
        model.dispose();
    }

    protected void solveKernel() throws GRBException {
        var timeLimit = Math.min(config.getTimeLimitKernel(), getRemainingTime());
        var model = solver.createModel(instance, timeLimit);

        var toDisable = variables.stream().filter(v -> !kernel.contains(v)).collect(Collectors.toList());
        model.disableVariables(toDisable);

        log.kernelStart(kernel.size());
        bestSolution = model.solve();
        log.solution(bestSolution.getObjective(), elapsedTime());

        model.write();
        model.dispose();
    }

    protected void iterateBuckets() throws GRBException {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (getRemainingTime() <= TIME_THRESHOLD) {
                log.timeLimit();
                return;
            }

            log.iterationStart(i);
            this.resetUsages();

            solveBuckets();
        }
    }

    protected void solveBuckets() throws GRBException {
        int count = 0; int count_solutions = 0;

        for (var b : buckets) {
            count++;

            Model model = this.buildModel(b, count);
            Solution solution = model.solve();

            if (!solution.isEmpty()) {
                count_solutions++;
                bestSolution = solution;

                // Prendi le variabili del bucket che compaiono nella nuova soluzione trovata,
                // aggiungile al kernel, e rimuovile dal bucket
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(variable -> this.kernel.addItem(variable));

                this.executeEject(selected, solution, count_solutions);

                model.write();
            } else {
                log.noSolution(elapsedTime());
            }

            model.dispose();

            if (getRemainingTime() <= TIME_THRESHOLD) {
                return;
            }
        }
    }

    protected void executeEject(List<Variable> selected, Solution solution, int count_solutions) {
        log.solution(selected.size(), kernel.size(), solution.getObjective(), elapsedTime());
    }

    protected double elapsedTime() {
        var time = (double)(System.nanoTime()-startTime);
        return time/1_000_000_000;
    }

    // Restituisce il tempo rimamente per l'esecuzione
    protected long getRemainingTime() {
        var time = config.getTimeLimit() - (long) elapsedTime();
        return time >= 0 ? time : 0;
    }

    protected Model buildModel(Bucket b, int count) throws GRBException {
        log.bucketStart(count, b.size());

        var timeLimit = Math.min(config.getTimeLimitBucket(), getRemainingTime());
        var model = solver.createModel(instance, timeLimit);

        // Disabilita le variabili che non sono ne nel kernel ne nel bucket
        var toDisable = variables.stream()
                .filter(v -> !kernel.contains(v) && !b.contains(v)).collect(Collectors.toList());
        model.disableVariables(toDisable);
        model.addBucketConstraint(b.getVariables());

        if (!bestSolution.isEmpty()) {
            // Se la soluzione iniziale non è vuota, imponi che l'eventuale nuova soluzione sia migliore
            model.addObjConstraint(bestSolution.getObjective());
            // Imposta la soluzione da cui il modello parte.
            model.readSolution(bestSolution);
        }

        return model;
    }

    /**
     * If an eject procedure is used then this function resets the usages of the variables in the kernel
     */
    protected void resetUsages() {}
}
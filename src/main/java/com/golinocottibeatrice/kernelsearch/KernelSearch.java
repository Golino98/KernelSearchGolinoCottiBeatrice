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
    private static final int TIME_THRESHOLD = 2;

    private final SearchConfiguration config;
    private final Logger log;
    private final Solver solver;
    private List<Bucket> buckets;
    private Kernel kernel;

    private final Instance instance;
    private long startTime;
    // Variabili del problema
    private List<Variable> variables;
    // Miglior soluzione trovata
    private Solution bestSolution;

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
        // Nei bucket vanno solo le variabili che non sono già nel kernel
        buckets = config.getBucketBuilder().build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);

        solveKernel();
        iterateBuckets();
        log.end(bestSolution.getObjective(), elapsedTime());

        return new SearchResult(bestSolution, elapsedTime(), elapsedTime() + TIME_THRESHOLD >= config.getTimeLimit());
    }

    private void solveRelaxation() throws GRBException {
        var model = solver.createModel(instance, config.getTimeLimit(), true);
        log.relaxStart();

        var solution = model.solve();
        log.solution(solution.getObjective(), elapsedTime());

        variables = solution.getVariables();
        model.dispose();
    }

    private void solveKernel() throws GRBException {
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

    private void iterateBuckets() throws GRBException {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (getRemainingTime() <= TIME_THRESHOLD) {
                log.timeLimit();
                return;
            }

            log.iterationStart(i);

            if (this.config.getEjectThreshold()>0) {
                this.kernel.resetUsages();
            }

            solveBuckets();
        }
    }

    private void solveBuckets() throws GRBException {
        int count = 0;
        int count_solutions = 0;

        for (var b : buckets) {
            log.bucketStart(count, b.size());
            count++;

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

            var solution = model.solve();

            if (!solution.isEmpty()) {
                count_solutions++;
                bestSolution = solution;

                // Prendi le variabili del bucket che compaiono nella nuova soluzione trovata,
                // aggiungile al kernel, e rimuovile dal bucket
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(variable -> {variable.setBucket(b); this.kernel.addItem(variable); b.removeItem(variable);});

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

    /**
     * Checks if an eject procedure is specified and executes it
     *
     * @param selected list of variables selected from the bucket
     * @param solution the current solution
     * @param count_solutions number of solutions visited in the current iteration
     */
    private void executeEject(List<Variable> selected, Solution solution, int count_solutions) {
        if (this.config.getEjectThreshold() > 0) {
            this.kernel.updateUsages(solution);
            List<Variable> removed_vars = this.kernel.checkForEject(this.config.getEjectThreshold(), count_solutions);

            removed_vars.forEach(variable -> {
                if (variable.getBucket() != null) {
                    variable.getBucket().addVariable(variable);
                }else {
                    this.buckets.get(0).addVariable(variable);
                }
            });

            log.solution(selected.size(), kernel.size(), solution.getObjective(), elapsedTime(), removed_vars.size());
        } else {
            log.solution(selected.size(), kernel.size(), solution.getObjective(), elapsedTime());
        }
    }

    private double elapsedTime() {
        var time = (double)(System.nanoTime()-startTime);
        return time/1_000_000_000;
    }

    // Restituisce il tempo rimamente per l'esecuzione
    private long getRemainingTime() {
        var time = config.getTimeLimit() - (long) elapsedTime();
        return time >= 0 ? time : 0;
    }
}
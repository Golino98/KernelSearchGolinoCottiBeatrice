package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.solver.*;
import gurobi.GRBException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementa il metodo della kernel search, usando GUROBI come risolutore.
 * I parametri di configurazione vengono letti da un'istanza di {@link Configuration}.
 */
public class KernelSearch {
    // Soglia tra il tempo trascorso e il tempo massimo di esecuzione
    // sotto il cui viene fermato il programma
    private static final int TIME_THRESHOLD = 1;

    private final SearchConfiguration config;
    private final Logger log;
    private final Solver solver;
    private List<Bucket> buckets;
    private Kernel kernel;

    private Instance instance;
    private double elapsedTime = 0;
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
        this.instance = config.getInstance();
        this.log = config.getLogger();
        this.solver = config.getSolver();
    }

    /**
     * Avvia la kernel search.
     *
     * @throws GRBException Errore di Gurobi.
     */
    public void start() throws GRBException {
        elapsedTime = 0;
        log.start(instance.getName(), Instant.now());

        solveRelaxation();

        config.getVariableSorter().sort(variables);
        kernel = config.getKernelBuilder().build(variables, config);
        // Nei bucket vanno solo le variabili che non sono già nel kernel
        buckets = config.getBucketBuilder().build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);

        solveKernel();
        iterateBuckets();
        log.end(bestSolution.getObjective(), elapsedTime);
    }

    private void solveRelaxation() throws GRBException {
        var model = solver.createModel(instance, config.getTimeLimit(), true);
        log.relaxStart();

        var solution = model.solve();
        log.solution(solution.getObjective(), elapsedTime);

        variables = solution.getVariables();
        elapsedTime += model.getElapsedTime();
        model.dispose();
    }

    private void solveKernel() throws GRBException {
        var timeLimit = Math.min(config.getTimeLimitKernel(), getRemainingTime());
        var model = solver.createModel(instance, timeLimit);

        var toDisable = variables.stream().filter(v -> !kernel.contains(v)).collect(Collectors.toList());
        model.disableVariables(toDisable);

        log.kernelStart();
        bestSolution = model.solve();
        elapsedTime += model.getElapsedTime();
        log.solution(bestSolution.getObjective(), elapsedTime);

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
            solveBuckets();
        }
    }

    private void solveBuckets() throws GRBException {
        int count = 0;

        for (var b : buckets) {
            log.bucketStart(count++);

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

            elapsedTime += model.getElapsedTime();
            if (!solution.isEmpty()) {
                bestSolution = solution;
                log.solution(solution.getObjective(), elapsedTime);

                // Prendi le variabili del bucket che compaiono nella nuova soluzione trovata,
                // aggiungile al kernel, e rimuovile dal bucket
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(kernel::addItem);
                selected.forEach(b::removeItem);

                model.write();
            } else {
                log.noSolution(elapsedTime);
            }

            model.dispose();

            if (getRemainingTime() <= TIME_THRESHOLD) {
                return;
            }
        }
    }


    // Restituisce il tempo rimamente per l'esecuzione
    private long getRemainingTime() {
        var time = config.getTimeLimit() - (long) elapsedTime;
        return time >= 0 ? time : 0;
    }
}
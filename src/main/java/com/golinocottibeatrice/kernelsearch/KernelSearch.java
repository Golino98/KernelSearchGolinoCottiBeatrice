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
        startTime = System.nanoTime();
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
        var q = solveBuckets();
        var initialValue = q - 1;
        while (q != -1) {
            if (getRemainingTime() <= TIME_THRESHOLD) {
                log.timeLimit();
                return;
            }

            var nb = q - 1;
            q = solveBuckets(nb);
            if (q == -1) {
                return;
            } else {
                nb = initialValue;
                q = solveBuckets(nb);
            }
        }
    }

    private int solveBuckets() throws GRBException {
        return solveBuckets(buckets.size());
    }

    private int solveBuckets(int nb) throws GRBException {
        int count = 0;
        int q = -1;

        for (var i = 0; i < nb; i++) {
            var b = buckets.get(i);
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
                q = i+1;
                bestSolution = solution;

                // Prendi le variabili del bucket che compaiono nella nuova soluzione trovata,
                // aggiungile al kernel, e rimuovile dal bucket
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(kernel::addItem);
                selected.forEach(b::removeItem);

                log.solution(selected.size(), kernel.size(), solution.getObjective(), elapsedTime());
                model.write();
            } else {
                log.noSolution(elapsedTime());
            }

            model.dispose();

            if (getRemainingTime() <= TIME_THRESHOLD) {
                break;
            }
        }

        return q;
    }

    private double elapsedTime() {
        var time = (double) (System.nanoTime() - startTime);
        return time / 1_000_000_000;
    }

    // Restituisce il tempo rimamente per l'esecuzione
    private long getRemainingTime() {
        var time = config.getTimeLimit() - (long) elapsedTime();
        return time >= 0 ? time : 0;
    }
}
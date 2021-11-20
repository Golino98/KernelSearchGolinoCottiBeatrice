package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.*;
import gurobi.GRBException;

import java.time.Duration;
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
    private static final int TIME_THRESHOLD = 3;

    private final Configuration config;
    private final Logger log;
    private Solver solver;
    private List<Bucket> buckets;
    private Kernel kernel;

    // Tempo di avvio della ricerca, usato per limitare il tempo di esecuzione
    private Instant startTime;
    // Variabili del problema
    private List<Variable> variables;
    // Miglior soluzione trovata
    private Solution bestSolution;

    /**
     * Crea una nuova istanza di kernel search.
     *
     * @param config La configurazione del metodo.
     */
    public KernelSearch(Configuration config) {
        this.config = config;
        this.log = config.getLogger();
    }

    /**
     * Avvia la kernel search.
     *
     * @throws GRBException Errore di Gurobi.
     */
    public void start() throws GRBException {
        solver = new Solver(new SolverConfiguration(config));

        startTime = Instant.now();
        log.start(startTime);

        solveRelaxation();

        config.getVariableSorter().sort(variables);
        kernel = config.getKernelBuilder().build(variables, config);
        // Nei bucket vanno solo le variabili che non sono già nel kernel
        buckets = config.getBucketBuilder().build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);

        solveKernel();
        iterateBuckets();

        solver.dispose();
    }

    private void solveRelaxation() throws GRBException {
        var model = solver.createModel(config.getInstance(), config.getTimeLimit(), true);
        log.relaxStart();

        var solution = model.solve();
        log.solution(solution.getObjective(), getElapsedTime());

        variables = solution.getVariables();
        model.dispose();
    }

    private void solveKernel() throws GRBException {
        var timeLimit = Math.min(config.getTimeLimitKernel(), getRemainingTime());
        var model = solver.createModel(config.getInstance(), timeLimit);

        var toDisable = variables.stream().filter(v -> !kernel.contains(v)).collect(Collectors.toList());
        model.disableVariables(toDisable);

        log.kernelStart();
        bestSolution = model.solve();
        log.solution(bestSolution.getObjective(), getElapsedTime());

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
            var model = solver.createModel(config.getInstance(), timeLimit);

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
                bestSolution = solution;
                log.solution(solution.getObjective(), getElapsedTime());

                // Prendi le variabili del bucket che compaiono nella nuova soluzione trovata,
                // aggiungile al kernel, e rimuovile dal bucket
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(kernel::addItem);
                selected.forEach(b::removeItem);

                model.write();
            } else {
                log.noSolution();
            }

            model.dispose();

            if (getRemainingTime() <= TIME_THRESHOLD) {
                return;
            }
        }
    }

    // Restituisce il tempo trascorso dall'avvio del programma
    private int getElapsedTime() {
        return (int) Duration.between(startTime, Instant.now()).getSeconds();
    }

    // Restituisce il tempo rimamente per l'esecuzione
    private int getRemainingTime() {
        return config.getTimeLimit() - getElapsedTime();
    }
}
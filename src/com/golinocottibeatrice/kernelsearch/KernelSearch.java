package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.*;
import gurobi.GRBException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementa il metodo della kernel search, usando Gurobi come risolutore.
 * I parametri di configurazione vengono letti da un'istanza di {@link Configuration}.
 */
public class KernelSearch {
    private static final int TIME_THRESHOLD = 3;

    private final Configuration config;
    private final Logger log;
    private Solver solver;
    private List<Bucket> buckets;
    private Kernel kernel;

    private Instant startTime;
    private Solution bestSolution;
    private List<Variable> variables;

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

        // Tempo di avvio della ricerca, usato per limitare il tempo di esecuzione
        startTime = Instant.now();
        log.start(startTime);

        solveRelaxation();
        config.getVariableSorter().sort(variables);

        kernel = config.getKernelBuilder().build(variables, config);
        buckets = config.getBucketBuilder().build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);
        solveKernel();
        iterateBuckets();

        solver.dispose();
    }

    // Risolve il rilassato del problema
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

        model.write(config.getSolPath());
        model.dispose();
    }

    private void iterateBuckets() throws GRBException {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (getRemainingTime() <= TIME_THRESHOLD) {
                return;
            }

            log.iterationStart(i);
            solveBuckets();
        }
    }

    private void solveBuckets() throws GRBException {
        int count = 0;

        for (Bucket b : buckets) {
            log.bucketStart(count++);

            var timeLimit = Math.min(config.getTimeLimitBucket(), getRemainingTime());
            var model = solver.createModel(config.getInstance(), timeLimit);

            var toDisable = variables.stream().filter(v -> !kernel.contains(v) && !b.contains(v)).collect(Collectors.toList());
            model.disableVariables(toDisable);
            model.addBucketConstraint(b.getVariables());

            if (!bestSolution.isEmpty()) {
                model.addObjConstraint(bestSolution.getObjective());
                model.readSolution(bestSolution);
            }

            var solution = model.solve();

            if (!solution.isEmpty()) {
                bestSolution = solution;
                log.solution(solution.getObjective(), getElapsedTime());
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(kernel::addItem);
                selected.forEach(b::removeItem);
                model.write(config.getSolPath());
            } else {
                log.noSolution();
            }

            if (getRemainingTime() <= TIME_THRESHOLD) {
                return;
            }

            model.dispose();
        }
    }

    private int getRemainingTime() {
        return config.getTimeLimit() - getElapsedTime();
    }

    private int getElapsedTime() {
        return (int) Duration.between(startTime, Instant.now()).getSeconds();
    }
}
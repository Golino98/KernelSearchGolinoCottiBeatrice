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
    private static final int TIME_THRESHOLD = 5;
    private static final String FORMAT_KERNEL = "\n\n[Solving kernel]\n";
    private static final String FORMAT_ITERATION = "\n\n[Iteration %d]\n";
    private static final String FORMAT_SOLVE_BUCKET = "\n<Solving bucket %d>\n";
    private static final String FORMAT_NEW_SOLUTION = "OBJ=%06.2f* -  TIME: +%ds\n";
    private static final String NO_SOLUTION_FOUND = "No solution found\n";

    private final Configuration config;
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
    }

    /**
     * Avvia la kernel search.
     *
     * @throws GRBException Errore di Gurobi.
     */
    public void start() throws GRBException {
        // Tempo di avvio della ricerca, usato per limitare il tempo di esecuzione
        startTime = Instant.now();
        solver = new Solver(new SolverConfiguration(config));

        var sorter = config.getVariableSorter();
        var kernelBuilder = config.getKernelBuilder();
        var bucketBuilder = config.getBucketBuilder();

        solveRelaxation();
        sorter.sort(variables);

        kernel = kernelBuilder.build(variables, config);
        buckets = bucketBuilder.build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);
        solveKernel();
        iterateBuckets();

        solver.dispose();
    }

    private void solveRelaxation() throws GRBException {
        var model = solver.createModel(config.getInstance(), config.getTimeLimit(), true);
        var solution = model.solve();
        variables = solution.getVariables();
        model.dispose();
    }

    private void solveKernel() throws GRBException {
        var timeLimit = Math.min(config.getTimeLimitKernel(), getRemainingTime());
        var model = solver.createModel(config.getInstance(), timeLimit, false);

        var toDisable = variables.stream().filter(v -> !kernel.contains(v)).collect(Collectors.toList());
        model.disableVariables(toDisable);

        System.out.print(FORMAT_KERNEL);
        bestSolution = model.solve();
        System.out.printf(FORMAT_NEW_SOLUTION, bestSolution.getObjective(), getElapsedTime());

        model.write(config.getSolPath());
        model.dispose();
    }

    private void iterateBuckets() throws GRBException {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (getRemainingTime() <= TIME_THRESHOLD) {
                return;
            }

            System.out.format(FORMAT_ITERATION, i);
            solveBuckets();
        }
    }

    private void solveBuckets() throws GRBException {
        int count = 0;

        for (Bucket b : buckets) {
            System.out.format(FORMAT_SOLVE_BUCKET, count++);

            var timeLimit = Math.min(config.getTimeLimitBucket(), getRemainingTime());
            var model = solver.createModel(config.getInstance(), timeLimit, false);

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
                System.out.printf(FORMAT_NEW_SOLUTION, solution.getObjective(), getElapsedTime());
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(kernel::addItem);
                selected.forEach(b::removeItem);
                model.write(config.getSolPath());
            } else {
                System.out.print(NO_SOLUTION_FOUND);
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
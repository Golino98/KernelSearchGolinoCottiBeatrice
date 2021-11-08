package com.golino.cotti.classes;

import com.golino.cotti.classes.solver.Variable;
import com.golino.cotti.classes.solver.Solver;
import com.golino.cotti.classes.solver.SolverConfiguration;
import com.golino.cotti.classes.solver.Solution;
import gurobi.GRBCallback;
import gurobi.GRBException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.golino.cotti.classes.Costanti.*;

/**
 * Implementa il metodo della kernel search, usando Gurobi come risolutore.
 * I parametri di configurazione vengono letti da un'istanza di {@link Configuration}.
 */
public class KernelSearch {
    private final Configuration config;
    private List<Variable> variables;
    private Solution bestSolution;
    private List<Bucket> buckets;
    private Kernel kernel;
    private GRBCallback callback;
    private final int timeThreshold = 5;
    private final List<List<Double>> objValues;
    private Instant startTime;

    /**
     * Crea una nuova istanza di kernel search.
     *
     * @param config La configurazione del metodo.
     */
    public KernelSearch(Configuration config) {
        this.config = config;
        objValues = new ArrayList<>();
    }

    /**
     * Avvia la kernel search.
     *
     * @return La soluzione trovata.
     * @throws GRBException Errore di Gurobi.
     */
    public Solution start() throws GRBException {
        // Tempo di avvio della ricerca, usato per limitare il tempo di esecuzione
        startTime = Instant.now();

        var sorter = config.getVariableSorter();
        var kernelBuilder = config.getKernelBuilder();
        var bucketBuilder = config.getBucketBuilder();
        // TODO: vedere cos'è la callback
        callback = new CustomCallback(config.getLogPath(), startTime);

        buildItems();
        sorter.sort(variables);

        kernel = kernelBuilder.build(variables, config);
        buckets = bucketBuilder.build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);
        solveKernel();
        iterateBuckets();

        return bestSolution;
    }

    private void buildItems() throws GRBException {
        var solver = new Solver(new SolverConfiguration(config, config.getTimeLimit(), true));
        var solution = solver.solve();
        variables = solution.getVariables();
    }

    private void solveKernel() throws GRBException {
        var timeLimit = Math.min(config.getTimeLimitKernel(), getRemainingTime());
        var solver = new Solver(new SolverConfiguration(config, timeLimit, false));

        var toDisable = variables.stream().filter(v -> !kernel.contains(v)).collect(Collectors.toList());
        solver.disableVariables(toDisable);
        solver.setCallback(callback);

        bestSolution = solver.solve();
        var objs = new ArrayList<Double>();
        objs.add(bestSolution.getObjective());
        objValues.add(objs);
        // TODO: what
        // model.exportSolution();
    }

    private void iterateBuckets() throws GRBException {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (getRemainingTime() <= timeThreshold) {
                return;
            }

            // TODO: che è sta merda
            if (i != 0) {
                objValues.add(new ArrayList<>());
            }

            System.out.format(FORMATTED_ITERATION, i);
            solveBuckets();
        }
    }

    private void solveBuckets() throws GRBException {
        int count = 0;

        for (Bucket b : buckets) {
            System.out.format(FORMATTED_SOLVE_BUCKET, count++);

            var timeLimit = Math.min(config.getTimeLimitBucket(), getRemainingTime());
            var solver = new Solver(new SolverConfiguration(config, timeLimit, false));

            var toDisable = variables.stream().filter(v -> !kernel.contains(v) && !b.contains(v)).collect(Collectors.toList());
            solver.disableVariables(toDisable);
            solver.addBucketConstraint(b.getVariables());

            if (!bestSolution.isEmpty()) {
                solver.addObjConstraint(bestSolution.getObjective());
                solver.readSolution(bestSolution);
            }

            solver.setCallback(callback);
            var solution = solver.solve();

            if (!solution.isEmpty()) {
                bestSolution = solution;
                var selected = solver.getSelectedVariables(b.getVariables());
                selected.forEach(kernel::addItem);
                selected.forEach(b::removeItem);
                solver.exportSolution();
            }
            if (!bestSolution.isEmpty())
                objValues.get(objValues.size() - 1).add(bestSolution.getObjective());
            else
                objValues.get(objValues.size() - 1).add(0.0);

            if (getRemainingTime() <= timeThreshold)
                return;
        }
    }

    private int getRemainingTime() {
        return (int) (config.getTimeLimit() - Duration.between(startTime, Instant.now()).getSeconds());
    }

    public List<List<Double>> getObjValues() {
        return objValues;
    }
}
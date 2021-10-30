package com.golino.cotti.classes;

import gurobi.GRBCallback;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KernelSearch {
    private final Configuration config;
    private List<Item> items;
    private Solution bestSolution;
    private List<Bucket> buckets;
    private Kernel kernel;
    private GRBCallback callback;
    private final int timeThreshold = 5;
    private final List<List<Double>> objValues;
    private Instant startTime;

    /**
     * @param config {@link Configuration} passata per all'inizio. Utilizzata per settare i parametri della kernel search
     */
    public KernelSearch(Configuration config) {
        this.config = config;
        bestSolution = new Solution();
        objValues = new ArrayList<>();
    }

    public Solution start() {
        startTime = Instant.now();

        var sorter = config.getItemSorter();
        var kernelBuilder = config.getKernelBuilder();
        var bucketBuilder = config.getBucketBuilder();
        callback = new CustomCallback(config.getLogPath(), startTime);
        items = buildItems();

        sorter.sort(items);
        kernel = kernelBuilder.build(items, config);
        buckets = bucketBuilder.build(items.stream()
                .filter(it -> !kernel.contains(it)).collect(Collectors.toList()), config);
        solveKernel();
        iterateBuckets();

        return bestSolution;
    }

    List<Item> buildItems() {
        Model model = new Model(config.getTimeLimit(), config, true); // time limit equal to the global time limit
        model.buildModel();
        model.solve();
        List<Item> items = new ArrayList<>();
        List<String> varNames = model.getVarNames();
        for (String v : varNames) {
            double value = model.getVarValue(v);
            double rc = model.getVarRC(v); // can be called only after solving the LP relaxation
            Item it = new Item(v, value, rc);
            items.add(it);
        }
        return items;
    }

    private void solveKernel() {
        Model model = new Model(Math.min(config.getTimeLimitKernel(), getRemainingTime()), config, false);
        model.buildModel();
        if (!bestSolution.isEmpty()) {
            model.readSolution(bestSolution);
        }

        List<Item> toDisable = items.stream().filter(it -> !kernel.contains(it)).collect(Collectors.toList());
        model.disableItems(toDisable);
        model.setCallback(callback);
        model.solve();
        if (model.hasSolution()) {
            bestSolution = model.getSolution();
            model.exportSolution();

            objValues.get(objValues.size() - 1).add(bestSolution.getObj());
        } else {
            objValues.get(objValues.size() - 1).add(0.0);
        }
    }

    private void iterateBuckets() {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (getRemainingTime() <= timeThreshold)
                return;
            if (i != 0)
                objValues.add(new ArrayList<>());

            System.out.println("\n\n\n\t\t******** Iteration " + i + " ********\n\n\n");
            solveBuckets();
        }
    }

    private void solveBuckets() {
        int count = 0;

        for (Bucket b : buckets) {
            System.out.println("\n\n\n\n\t\t** Solving bucket " + count++ + " **\n");
            List<Item> toDisable = items.stream().filter(it -> !kernel.contains(it) && !b.contains(it)).collect(Collectors.toList());

            Model model = new Model(Math.min(config.getTimeLimitBucket(), getRemainingTime()), config, false);
            model.buildModel();

            model.disableItems(toDisable);
            model.addBucketConstraint(b.getItems()); // can we use this constraint regardless of the type of variables chosen as items?

            if (!bestSolution.isEmpty()) {
                model.addObjConstraint(bestSolution.getObj());
                model.readSolution(bestSolution);
            }

            model.setCallback(callback);
            model.solve();

            if (model.hasSolution()) {
                bestSolution = model.getSolution();
                List<Item> selected = model.getSelectedItems(b.getItems());
                selected.forEach(kernel::addItem);
                selected.forEach(b::removeItem);
                model.exportSolution();
            }
            if (!bestSolution.isEmpty())
                objValues.get(objValues.size() - 1).add(bestSolution.getObj());
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
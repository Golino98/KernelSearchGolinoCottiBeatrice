package com.golino.cotti.classes;

import gurobi.GRBCallback;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KernelSearch {
    private final String instPath;
    private final String logPath;
    private final Configuration config;
    private List<Item> items;
    private ItemSorter sorter;
    private BucketBuilder bucketBuilder;
    private KernelBuilder kernelBuilder;
    private int tlim;
    private Solution bestSolution;
    private List<Bucket> buckets;
    private Kernel kernel;
    private int tlimKernel;
    private int tlimBucket;
    private int numIterations;
    private GRBCallback callback;
    private final int timeThreshold = 5;
    private final List<List<Double>> objValues;

    private Instant startTime;

    /**
     * @param instPath
     * @param logPath
     * @param config
     */
    public KernelSearch(String instPath, String logPath, Configuration config) {
        this.instPath = instPath;
        this.logPath = logPath;
        this.config = config;
        bestSolution = new Solution();
        objValues = new ArrayList<>();
        configure(config);
    }

    /**
     * Per quale motivo utilizziamo direttamente il valore della config passato dal metodo costruttore e non utilizziamo
     * quello passato come parametro all'interno del metodo?
     *
     * @param configuration
     */
    private void configure(Configuration configuration) {
        sorter = config.getItemSorter();
        tlim = config.getTimeLimit();
        bucketBuilder = config.getBucketBuilder();
        kernelBuilder = config.getKernelBuilder();
        tlimKernel = config.getTimeLimitKernel();
        numIterations = config.getNumIterations();
        tlimBucket = config.getTimeLimitBucket();
    }

    public Solution start() {
        startTime = Instant.now();
        callback = new CustomCallback(logPath, startTime);
        items = buildItems();
        sorter.sort(items);
        kernel = kernelBuilder.build(items, config);
        buckets = bucketBuilder.build(items.stream().filter(it -> !kernel.contains(it)).collect(Collectors.toList()), config);
        solveKernel();
        iterateBuckets();
        return bestSolution;
    }

    List<Item> buildItems() {
        Model model = new Model(instPath, logPath, config.getTimeLimit(), config, true); // time limit equal to the global time limit
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
        Model model = new Model(instPath, logPath, Math.min(tlimKernel, getRemainingTime()), config, false);
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
        for (int i = 0; i < numIterations; i++) {
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

            Model model = new Model(instPath, logPath, Math.min(tlimBucket, getRemainingTime()), config, false);
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
                selected.forEach(it -> kernel.addItem(it));
                selected.forEach(it -> b.removeItem(it));
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
        return (int) (tlim - Duration.between(startTime, Instant.now()).getSeconds());
    }

    public List<List<Double>> getObjValues() {
        return objValues;
    }
}
package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.additions.DominanceList;
import com.golinocottibeatrice.kernelsearch.additions.RepetitionCounter;
import com.golinocottibeatrice.kernelsearch.bucket.BucketBuilder;
import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.kernel.KernelBuilder;
import com.golinocottibeatrice.kernelsearch.solver.Solver;
import com.golinocottibeatrice.kernelsearch.sorter.VariableSorter;
import gurobi.GRBEnv;

public class SearchConfiguration {
    // Limiti di tempo
    private int timeLimit;
    private int timeLimitKernel;
    private int timeLimitBucket;

    // Parametri della kernel search
    private int numIterations;
    private double kernelSize;
    private double bucketSize;
    private VariableSorter sorter;
    private BucketBuilder bucketBuilder;
    private KernelBuilder kernelBuilder;
    private Instance instance;
    private Logger logger;
    private Solver solver;
    private GRBEnv grbEnv;

    // Funzionalità aggiuntive
    private boolean ejectEnabled;
    private int ejectThreshold;
    private boolean isRepCtrEnabled;
    private RepetitionCounter repetitionCounter;
    private boolean isItemDomEnabled;
    private DominanceList dominanceList;
    private boolean isHeuristicEnabled;
    private boolean repCtrEnabled;
    private int repCtrThreshold;
    private int repCtrPersistence;
    private double overlapRatio;

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getTimeLimitKernel() {
        return timeLimitKernel;
    }

    public void setTimeLimitKernel(int timeLimitKernel) {
        this.timeLimitKernel = timeLimitKernel;
    }

    public int getTimeLimitBucket() {
        return timeLimitBucket;
    }

    public void setTimeLimitBucket(int timeLimitBucket) {
        this.timeLimitBucket = timeLimitBucket;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public double getKernelSize() {
        return kernelSize;
    }

    public void setKernelSize(double kernelSize) {
        this.kernelSize = kernelSize;
    }

    public double getBucketSize() {
        return bucketSize;
    }

    public void setBucketSize(double bucketSize) {
        this.bucketSize = bucketSize;
    }

    public VariableSorter getVariableSorter() {
        return sorter;
    }

    public void setVariableSorter(VariableSorter sorter) {
        this.sorter = sorter;
    }

    public BucketBuilder getBucketBuilder() {
        return bucketBuilder;
    }

    public void setBucketBuilder(BucketBuilder bucketBuilder) {
        this.bucketBuilder = bucketBuilder;
    }

    public KernelBuilder getKernelBuilder() {
        return kernelBuilder;
    }

    public void setKernelBuilder(KernelBuilder kernelBuilder) {
        this.kernelBuilder = kernelBuilder;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Solver getSolver() {
        return solver;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public boolean isEjectEnabled() {
        return ejectEnabled;
    }

    public void setEjectEnabled(boolean ejectEnabled) {
        this.ejectEnabled = ejectEnabled;
    }

    public int getEjectThreshold() {
        return ejectThreshold;
    }

    public void setEjectThreshold(int ejectThreshold) {
        this.ejectThreshold = ejectThreshold;
    }

    public boolean isRepCtrEnabled() {
        return isRepCtrEnabled;
    }

    public void setRepCtrEnabled(boolean repCtrEnabled) {
        isRepCtrEnabled = repCtrEnabled;
    }

    public RepetitionCounter getRepetitionCounter() {
        return repetitionCounter;
    }

    public void setRepetitionCounter(RepetitionCounter repetitionCounter) {
        this.repetitionCounter = repetitionCounter;
    }

    public boolean isItemDomEnabled() {
        return isItemDomEnabled;
    }

    public void setItemDomEnabled(boolean itemDomEnabled) {
        isItemDomEnabled = itemDomEnabled;
    }
    public DominanceList getDominanceList() {
        return dominanceList;
    }

    public void setDominanceList(DominanceList dominanceList) {
        this.dominanceList = dominanceList;
    }

    public void setOverlapRatio(double ratio) {
        this.overlapRatio = ratio;
    }

    public double getOverlapRatio() {
        return this.overlapRatio;
    }

    public boolean isHeuristicEnabled() {
        return isHeuristicEnabled;
    }

    public void setHeuristicEnabled(boolean heuristicEnabled) {
        isHeuristicEnabled = heuristicEnabled;
    }

    public GRBEnv getGrbEnv() {
        return grbEnv;
    }

    public void setGrbEnv(GRBEnv grbEnv) {
        this.grbEnv = grbEnv;
    }
}

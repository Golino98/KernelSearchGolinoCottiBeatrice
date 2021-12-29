package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.bucket.BucketBuilder;
import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.kernel.KernelBuilder;
import com.golinocottibeatrice.kernelsearch.solver.Solver;
import com.golinocottibeatrice.kernelsearch.sorter.VariableSorter;

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

    // Funzionalità aggiuntive
    private boolean ejectEnabled;
    private int ejectThreshold;
    private boolean repCtrEnabled;
    private int repCtrThreshold;
    private int repCtrPersistence;

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
        return repCtrEnabled;
    }

    public void setRepCtrEnabled(boolean repCtrEnabled) {
        this.repCtrEnabled = repCtrEnabled;
    }

    public int getRepCtrThreshold() {
        return repCtrThreshold;
    }

    public void setRepCtrThreshold(int repCtrThreshold) {
        this.repCtrThreshold = repCtrThreshold;
    }

    public int getRepCtrPersistence() {
        return repCtrPersistence;
    }

    public void setRepCtrPersistence(int repCtrPersistence) {
        this.repCtrPersistence = repCtrPersistence;
    }


}

package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.instance.Instance;

/**
 * Contenitore per le informazione di configurazione del progetto.
 */
public class Configuration {
    // Parametri di Gurobi
    private int numThreads;
    private int presolve;
    private double mipGap;
    private String logPath;

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

    public BucketBuilder getBucketBuilder() {
        return bucketBuilder;
    }

    public void setBucketBuilder(BucketBuilder bucketBuilder) {
        this.bucketBuilder = bucketBuilder;
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

    public KernelBuilder getKernelBuilder() {
        return kernelBuilder;
    }

    public void setKernelBuilder(KernelBuilder kernelBuilder) {
        this.kernelBuilder = kernelBuilder;
    }

    public double getKernelSize() {
        return kernelSize;
    }

    public void setKernelSize(double kernelSize) {
        this.kernelSize = kernelSize;
    }

    public double getMipGap() {
        return mipGap;
    }

    public void setMipGap(double mipGap) {
        this.mipGap = mipGap;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getPresolve() {
        return presolve;
    }

    public void setPresolve(int presolve) {
        this.presolve = presolve;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getTimeLimitBucket() {
        return timeLimitBucket;
    }

    public void setTimeLimitBucket(int timeLimitBucket) {
        this.timeLimitBucket = timeLimitBucket;
    }

    public int getTimeLimitKernel() {
        return timeLimitKernel;
    }

    public void setTimeLimitKernel(int timeLimitKernel) {
        this.timeLimitKernel = timeLimitKernel;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }
}
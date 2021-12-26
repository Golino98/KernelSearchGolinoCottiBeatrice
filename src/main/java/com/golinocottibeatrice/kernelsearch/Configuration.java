package com.golinocottibeatrice.kernelsearch;

/**
 * Contenitore per le informazione di configurazione del progetto, come lette da file.
 */
public class Configuration {
    private int numThreads;
    private int presolve;
    private double mipGap;
    private String logDir;
    private int timeLimit;
    private int timeLimitKernel;
    private int timeLimitBucket;
    private int numIterations;
    private double kernelSize;
    private double bucketSize;
    private int sorter;
    private int bucketBuilder;
    private int kernelBuilder;
    private String instPath = "";
    private String runName = "";

    public int getBucketBuilder() {
        return bucketBuilder;
    }

    public void setBucketBuilder(int bucketBuilder) {
        this.bucketBuilder = bucketBuilder;
    }

    public double getBucketSize() {
        return bucketSize;
    }

    public void setBucketSize(double bucketSize) {
        this.bucketSize = bucketSize;
    }

    public int getVariableSorter() {
        return sorter;
    }

    public void setVariableSorter(int sorter) {
        this.sorter = sorter;
    }

    public int getKernelBuilder() {
        return kernelBuilder;
    }

    public void setKernelBuilder(int kernelBuilder) {
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

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public String getInstPath() {
        return instPath;
    }

    public void setInstPath(String instPath) {
        this.instPath = instPath;
    }

    public String getRunName() {
        return runName;
    }

    public void setRunName(String runName) {
        this.runName = runName;
    }
}
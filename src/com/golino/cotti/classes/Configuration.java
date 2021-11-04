package com.golino.cotti.classes;

/**
 * Contenitore per le informazione di configurazione del progetto.
 */
public class Configuration {
    // Parametri di Gurobi
    private int numThreads;
    private int presolve;
    private double mipGap;

    // Limiti di tempo
    private int timeLimit;
    private int timeLimitKernel;
    private int timeLimitBucket;

    // Parametri della kernel search
    private int numIterations;
    private double kernelSize;
    private double bucketSize;
    private ItemSorter sorter;
    private BucketBuilder bucketBuilder;
    private KernelBuilder kernelBuilder;

    // File di output di Gurobi
    private String logPath;

    // File contenente l'istanza del problema
    private String instPath;

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

    public ItemSorter getItemSorter() {
        return sorter;
    }

    public void setItemSorter(ItemSorter sorter) {
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

    public String getInstPath() {
        return instPath;
    }

    public void setInstPath(String instPath) {
        this.instPath = instPath;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
}
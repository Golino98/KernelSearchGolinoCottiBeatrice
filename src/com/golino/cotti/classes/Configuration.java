package com.golino.cotti.classes;

public class Configuration {
    private int numThreads;
    private double mipGap;
    private int presolve;
    private int timeLimit;
    private ItemSorter sorter;
    private double kernelSize;
    private double bucketSize;
    private BucketBuilder bucketBuilder;
    private KernelBuilder kernelBuilder;
    private int timeLimitKernel;
    private int numIterations;
    private int timeLimitBucket;
    private String instPath;
    private String logPath;

    /**
     * Metodo getter per la variabile bucketBuilder
     *
     * @return della variabile bucketBuilder, di tipo BucketBuilder
     */
    public BucketBuilder getBucketBuilder() {
        return bucketBuilder;
    }

    /**
     * @param bucketBuilder, di tipo {@link BucketBuilder} utilizzata per andare a
     *                       settare/modificare il valore della variabile bucketBuilder
     */
    public void setBucketBuilder(BucketBuilder bucketBuilder) {
        this.bucketBuilder = bucketBuilder;
    }

    /**
     * Metodo getter per la variabile bucketSize
     *
     * @return della variabile bucketSize, di tipo double
     */
    public double getBucketSize() {
        return bucketSize;
    }

    /**
     * @param bucketSize, di tipo double utilizzata per andare a
     *                    settare/modificare il valore della variabile bucketSize
     */
    public void setBucketSize(double bucketSize) {
        this.bucketSize = bucketSize;
    }

    /**
     * Metodo getter per la variabile sorter
     *
     * @return della variabile sorter, di tipo ItemSorter
     */
    public ItemSorter getItemSorter() {
        return sorter;
    }

    /**
     * @param sorter, di tipo {@link ItemSorter} utilizzata per andare a
     *                settare/modificare il valore della variabile sorter
     */
    public void setItemSorter(ItemSorter sorter) {
        this.sorter = sorter;
    }

    /**
     * Metodo getter per la variabile kernelBuilder
     *
     * @return della variabile kernelBuilder, di tipo KernelBuilder
     */
    public KernelBuilder getKernelBuilder() {
        return kernelBuilder;
    }

    /**
     * @param kernelBuilder, di tipo {@link KernelBuilder} utilizzata per andare a
     *                       settare/modificare il valore della variabile kernelBuilder
     */
    public void setKernelBuilder(KernelBuilder kernelBuilder) {
        this.kernelBuilder = kernelBuilder;
    }

    /**
     * Metodo getter per la variabile kernelSize
     *
     * @return della variabile bucketBuilder, di tipo double
     */
    public double getKernelSize() {
        return kernelSize;
    }

    /**
     * @param kernelSize, di tipo double utilizzata per andare a
     *                    settare/modificare il valore della variabile kernelSize
     */
    public void setKernelSize(double kernelSize) {
        this.kernelSize = kernelSize;
    }

    /**
     * Metodo getter per la variabile mipGap
     *
     * @return della variabile mipGap, di tipo double
     */
    public double getMipGap() {
        return mipGap;
    }

    /**
     * @param mipGap, di tipo double utilizzata per andare a
     *                settare/modificare il valore della variabile mipGap
     */
    public void setMipGap(double mipGap) {
        this.mipGap = mipGap;
    }

    /**
     * Metodo getter per la variabile numIterations
     *
     * @return della variabile numIterations, di tipo int
     */
    public int getNumIterations() {
        return numIterations;
    }

    /**
     * @param numIterations, di tipo int utilizzata per andare a
     *                       settare/modificare il valore della variabile numIterations
     */
    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    /**
     * Metodo getter per la variabile numThreads
     *
     * @return della variabile numThreads, di tipo int
     */
    public int getNumThreads() {
        return numThreads;
    }

    /**
     * @param numThreads, di tipo int utilizzata per andare a
     *                    settare/modificare il valore della variabile bucketBuilder
     */
    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    /**
     * Metodo getter per la variabile presolve
     *
     * @return della variabile presolve, di tipo int
     */
    public int getPresolve() {
        return presolve;
    }

    /**
     * @param presolve, di tipo int utilizzata per andare a
     *                  settare/modificare il valore della variabile presolve
     */
    public void setPresolve(int presolve) {
        this.presolve = presolve;
    }

    /**
     * Metodo getter per la variabile timeLimit
     *
     * @return della variabile timeLimit, di tipo int (espresso in secondi)
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * @param timeLimit, di tipo int utilizzata per andare a
     *                   settare/modificare il valore della variabile timeLimit (espresso in secondi)
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * Metodo getter per la variabile timeLimitBucket
     *
     * @return della variabile timeLimitBucket, di tipo int (espresso in secondi)
     */
    public int getTimeLimitBucket() {
        return timeLimitBucket;
    }

    /**
     * @param timeLimitBucket, di tipo int utilizzata per andare a
     *                         settare/modificare il valore della variabile timeLimitBucket (espresso in secondi)
     */
    public void setTimeLimitBucket(int timeLimitBucket) {
        this.timeLimitBucket = timeLimitBucket;
    }

    /**
     * Metodo getter per la variabile timeLimitKernel
     *
     * @return della variabile timeLimitKernel, di tipo int (espresso in secondi)
     */
    public int getTimeLimitKernel() {
        return timeLimitKernel;
    }

    /**
     * @param timeLimitKernel, di tipo int utilizzata per andare a
     *                         settare/modificare il valore della variabile timeLimitKernel (espresso in secondi)
     */
    public void setTimeLimitKernel(int timeLimitKernel) {
        this.timeLimitKernel = timeLimitKernel;
    }

    public String getInstPath() {return instPath;}

    public void setInstPath(String instPath) {this.instPath=instPath;}

    public String getLogPath() {return logPath;}

    public void setLogPath(String logPath) {this.logPath=logPath;}
}
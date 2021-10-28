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

    public Configuration() {
    }

    /**
     * Metodo getters per la variabile bucketBuilder
     *
     * @return della variabile bucketBuilder, di tipo BucketBuilder
     */
    public BucketBuilder getBucketBuilder() {
        return bucketBuilder;
    }

    /**
     * Metodo getters per la variabile bucketSize
     *
     * @return della variabile bucketSize, di tipo double
     */
    public double getBucketSize() {
        return bucketSize;
    }

    /**
     * Metodo getters per la variabile sorter
     *
     * @return della variabile sorter, di tipo ItemSorter
     */
    public ItemSorter getItemSorter() {
        return sorter;
    }

    /**
     * Metodo getters per la variabile kernelBuilder
     *
     * @return della variabile kernelBuilder, di tipo KernelBuilder
     */
    public KernelBuilder getKernelBuilder() {
        return kernelBuilder;
    }

    /**
     * Metodo getters per la variabile kernelSize
     *
     * @return della variabile bucketBuilder, di tipo double
     */
    public double getKernelSize() {
        return kernelSize;
    }

    /**
     * Metodo getters per la variabile mipGap
     *
     * @return della variabile mipGap, di tipo double
     */
    public double getMipGap() {
        return mipGap;
    }

    /**
     * Metodo getters per la variabile numIterations
     *
     * @return della variabile numIterations, di tipo int
     */
    public int getNumIterations() {
        return numIterations;
    }

    /**
     * Metodo getters per la variabile numThreads
     *
     * @return della variabile numThreads, di tipo int
     */
    public int getNumThreads() {
        return numThreads;
    }

    /**
     * Metodo getters per la variabile presolve
     *
     * @return della variabile presolve, di tipo int
     */
    public int getPresolve() {
        return presolve;
    }

    /**
     * Metodo getters per la variabile timeLimit
     *
     * @return della variabile timeLimit, di tipo int (espresso in secondi)
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * Metodo getters per la variabile timeLimitBucket
     *
     * @return della variabile timeLimitBucket, di tipo int (espresso in secondi)
     */
    public int getTimeLimitBucket() {
        return timeLimitBucket;
    }

    /**
     * Metodo getters per la variabile timeLimitKernel
     *
     * @return della variabile timeLimitKernel, di tipo int (espresso in secondi)
     */
    public int getTimeLimitKernel() {
        return timeLimitKernel;
    }

    /**
     * @param bucketBuilder, di tipo {@link BucketBuilder} utilizzata per andare a
     *                       settare/modificare il valore della variabile bucketBuilder
     */
    public void setBucketBuilder(BucketBuilder bucketBuilder) {
        this.bucketBuilder = bucketBuilder;
    }

    /**
     * @param bucketSize, di tipo double utilizzata per andare a
     *                    settare/modificare il valore della variabile bucketSize
     */
    public void setBucketSize(double bucketSize) {
        this.bucketSize = bucketSize;
    }

    /**
     * @param sorter, di tipo {@link ItemSorter} utilizzata per andare a
     *                settare/modificare il valore della variabile sorter
     */
    public void setItemSorter(ItemSorter sorter) {
        this.sorter = sorter;
    }

    /**
     * @param kernelBuilder, di tipo {@link KernelBuilder} utilizzata per andare a
     *                       settare/modificare il valore della variabile kernelBuilder
     */
    public void setKernelBuilder(KernelBuilder kernelBuilder) {
        this.kernelBuilder = kernelBuilder;
    }

    /**
     * @param kernelSize, di tipo double utilizzata per andare a
     *                    settare/modificare il valore della variabile kernelSize
     */
    public void setKernelSize(double kernelSize) {
        this.kernelSize = kernelSize;
    }

    /**
     * @param mipGap, di tipo double utilizzata per andare a
     *                settare/modificare il valore della variabile mipGap
     */
    public void setMipGap(double mipGap) {
        this.mipGap = mipGap;
    }

    /**
     * @param numIterations, di tipo int utilizzata per andare a
     *                       settare/modificare il valore della variabile numIterations
     */
    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    /**
     * @param numThreads, di tipo int utilizzata per andare a
     *                    settare/modificare il valore della variabile bucketBuilder
     */
    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    /**
     * @param presolve, di tipo int utilizzata per andare a
     *                  settare/modificare il valore della variabile presolve
     */
    public void setPresolve(int presolve) {
        this.presolve = presolve;
    }

    /**
     * @param timeLimit, di tipo int utilizzata per andare a
     *                   settare/modificare il valore della variabile timeLimit (espresso in secondi)
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * @param timeLimitBucket, di tipo int utilizzata per andare a
     *                         settare/modificare il valore della variabile timeLimitBucket (espresso in secondi)
     */
    public void setTimeLimitBucket(int timeLimitBucket) {
        this.timeLimitBucket = timeLimitBucket;
    }

    /**
     * @param timeLimitKernel, di tipo int utilizzata per andare a
     *                         settare/modificare il valore della variabile timeLimitKernel (espresso in secondi)
     */
    public void setTimeLimitKernel(int timeLimitKernel) {
        this.timeLimitKernel = timeLimitKernel;
    }
}
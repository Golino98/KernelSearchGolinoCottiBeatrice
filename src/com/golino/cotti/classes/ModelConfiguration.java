package com.golino.cotti.classes;

public class ModelConfiguration {
    private String logPath;
    private String instPath;
    private int numThreads;
    private int presolve;
    private double mipGap;
    private int timeLimit;
    private boolean lpRelaxation;
    @SuppressWarnings("FieldCanBeLocal")
    private final double positiveThreshold = 1e-5;

    public ModelConfiguration(Configuration config, int timelimit,boolean lpRelaxation){
        this.logPath=config.getLogPath();
        this.instPath=config.getInstPath();
        this.numThreads=config.getNumThreads();
        this.presolve=config.getPresolve();
        this.mipGap=config.getMipGap();
        this.timeLimit=timelimit;
        this.lpRelaxation=lpRelaxation;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getInstPath() {
        return instPath;
    }

    public void setInstPath(String instPath) {
        this.instPath = instPath;
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

    public double getMipGap() {
        return mipGap;
    }

    public void setMipGap(double mipGap) {
        this.mipGap = mipGap;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isLpRelaxation() {
        return lpRelaxation;
    }

    public void setLpRelaxation(boolean lpRelaxation) {
        this.lpRelaxation = lpRelaxation;
    }

    public double getPositiveThreshold() {
        return positiveThreshold;
    }
}

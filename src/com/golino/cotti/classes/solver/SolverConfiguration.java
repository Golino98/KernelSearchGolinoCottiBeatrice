package com.golino.cotti.classes.solver;

import com.golino.cotti.classes.Configuration;
import com.golino.cotti.classes.instance.Instance;

/**
 * Rappresenta la configurazione necessaria al risolutore
 * del problema.
 */
public class SolverConfiguration {
    private String logPath;
    private Instance instance;
    private int numThreads;
    private int presolve;
    private double mipGap;
    private int timeLimit;
    private boolean lpRelaxation;
    private static final double positiveThreshold = 1e-5;

    public SolverConfiguration(Configuration config, int timelimit, boolean lpRelaxation) {
        this.logPath = config.getLogPath();
        this.instance = config.getInstance();
        this.numThreads = config.getNumThreads();
        this.presolve = config.getPresolve();
        this.mipGap = config.getMipGap();
        this.timeLimit = timelimit;
        this.lpRelaxation = lpRelaxation;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
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

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }
}

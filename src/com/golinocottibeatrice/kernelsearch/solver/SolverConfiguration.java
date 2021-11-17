package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.Configuration;

/**
 * Rappresenta la configurazione usata dal risolutore.
 */
public class SolverConfiguration {
    private final String logPath;
    private final int numThreads;
    private final int presolve;
    private final double mipGap;

    public SolverConfiguration(Configuration config) {
        this.logPath = config.getLogPath();
        this.numThreads = config.getNumThreads();
        this.presolve = config.getPresolve();
        this.mipGap = config.getMipGap();
    }

    public String getLogPath() {
        return logPath;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public int getPresolve() {
        return presolve;
    }

    public double getMipGap() {
        return mipGap;
    }
}

package com.golinocottibeatrice.kernelsearch.solver;

/**
 * Rappresenta la configurazione usata dal risolutore.
 */
public class SolverConfiguration {
    private String logDir;
    private int numThreads;
    private int presolve;
    private double mipGap;

    public String getLogDir() {
        return logDir;
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

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public void setPresolve(int presolve) {
        this.presolve = presolve;
    }

    public void setMipGap(double mipGap) {
        this.mipGap = mipGap;
    }
}

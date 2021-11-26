package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import gurobi.GRBEnv;

/**
 * Rappresenta la configurazione di un modello.
 */
class ModelConfiguration {
    private GRBEnv env;
    private String logPath;
    private String solPath;
    private Instance instance;
    private long timeLimit;
    private boolean isLpRelaxation;

    public GRBEnv getEnv() {
        return env;
    }

    public void setEnv(GRBEnv env) {
        this.env = env;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isLpRelaxation() {
        return isLpRelaxation;
    }

    public void setLpRelaxation(boolean lpRelaxation) {
        isLpRelaxation = lpRelaxation;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getSolPath() {
        return solPath;
    }

    public void setSolPath(String solPath) {
        this.solPath = solPath;
    }
}

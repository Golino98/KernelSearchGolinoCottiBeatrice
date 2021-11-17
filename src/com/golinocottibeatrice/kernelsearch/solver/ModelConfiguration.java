package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import gurobi.GRBEnv;

/**
 * Rappresenta la configurazione di un modello.
 */
class ModelConfiguration {
    private GRBEnv env;
    private Instance instance;
    private int timeLimit;
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

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isLpRelaxation() {
        return isLpRelaxation;
    }

    public void setLpRelaxation(boolean lpRelaxation) {
        isLpRelaxation = lpRelaxation;
    }
}

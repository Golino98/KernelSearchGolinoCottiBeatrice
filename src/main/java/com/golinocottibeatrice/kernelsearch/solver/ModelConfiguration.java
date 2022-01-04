package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import gurobi.GRBEnv;
import gurobi.GRBVar;

import java.util.List;

/**
 * Rappresenta la configurazione di un modello.
 */
class ModelConfiguration {
    private GRBEnv env;
    private String solPath;
    private Instance instance;
    private long timeLimit;
    private boolean isLpRelaxation;
    private GRBVar[][] grbVars;
    private List<Variable> variables;

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

    public String getSolPath() {
        return solPath;
    }

    public void setSolPath(String solPath) {
        this.solPath = solPath;
    }

    public GRBVar[][] getGrbVars() {
        return grbVars;
    }

    public void setGrbVars(GRBVar[][] grbVars) {
        this.grbVars = grbVars;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }
}

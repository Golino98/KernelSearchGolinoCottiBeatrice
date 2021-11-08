package com.golino.cotti.classes.solver;

import java.util.List;

/**
 * Rappresenta una soluzione del problema MKP.
 */
public class Solution {
    private final double objective;
    private final List<Variable> variables;

    /**
     * Crea una nuova soluzione.
     *
     * @param objective Valore della funzione obiettivo.
     * @param variables Lista contenente il valore delle variabili.
     */
    public Solution(double objective, List<Variable> variables) {
        this.objective = objective;
        this.variables = variables;
    }

    public double getObjective() {
        return objective;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public double getVariableValue(String name) {
        for (var v : variables) {
            if (v.getName().equals(name)) {
                return v.getValue();
            }
        }
        throw new IllegalArgumentException();
    }

    public boolean isEmpty() {
        return false;
    }
}

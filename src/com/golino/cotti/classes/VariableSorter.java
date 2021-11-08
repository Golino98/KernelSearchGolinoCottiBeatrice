package com.golino.cotti.classes;

import com.golino.cotti.classes.solver.Variable;

import java.util.List;

/**
 * Rappresenta un ordinatore di variabili, che opera usando un certo criterio.
 */
public interface VariableSorter {
    void sort(List<Variable> variables);
}

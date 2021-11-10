package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.Comparator;
import java.util.List;

/**
 * Ordina le variabili facendo il confronto prima sul valore (in modo inverso)
 * e poi sul RC.
 */
public class VariableSorterByValueAndAbsoluteRC implements VariableSorter {
    @Override
    public void sort(List<Variable> variables) {
        variables.sort(Comparator.comparing(Variable::getValue).reversed().thenComparing(Variable::getRC));
    }
}

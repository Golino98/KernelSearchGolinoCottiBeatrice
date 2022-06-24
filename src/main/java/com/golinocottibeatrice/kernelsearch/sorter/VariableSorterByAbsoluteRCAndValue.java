package com.golinocottibeatrice.kernelsearch.sorter;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.Comparator;
import java.util.List;

public class VariableSorterByAbsoluteRCAndValue implements VariableSorter {

    //Potrebbe essere meglio fare
    //variables.sort(Comparator.comparing(Variable::getRC).reversed.thenComparing(Variable::getValue));
    @Override
    public void sort(List<Variable> variables) {
        variables.sort(Comparator.comparing(Variable::getRC).thenComparing(Variable::getValue).reversed());
    }
}

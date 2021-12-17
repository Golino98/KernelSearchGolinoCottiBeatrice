package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.Comparator;
import java.util.List;

public class VariableSorterByProfitDivideWeight implements VariableSorter{

    @Override
    public void sort(List<Variable> variables) {
        variables.sort(Comparator.comparing(Variable::getProfitDividedByWeight).reversed().thenComparing(Variable::getRC).reversed());
    }
}

package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.Comparator;
import java.util.List;

public class VariableSorterByValueProfitWeightAndRC implements VariableSorter {

    @Override
    public void sort(List<Variable> variables) {
        Comparator<Variable> valueComparator = Comparator
                .comparingDouble(v -> {
                    double profit = v.getProfit();
                    double weight = v.getWeight();
                    return v.getValue() * profit / weight;
                });

        variables.sort(valueComparator.reversed().thenComparing(Variable::getRC));
    }
}

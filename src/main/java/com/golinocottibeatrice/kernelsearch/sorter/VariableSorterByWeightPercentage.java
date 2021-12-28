package com.golinocottibeatrice.kernelsearch.sorter;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.Comparator;
import java.util.List;

public class VariableSorterByWeightPercentage implements VariableSorter{
    @Override
    public void sort(List<Variable> variables) {
        Comparator<Variable> valueComparator =Comparator
                .comparingDouble(v -> {
                    double knap_capacity = v.getKnapsackCapacity();
                    double weight = v.getWeight();
                    return weight/knap_capacity;
                });

        variables.sort(valueComparator.reversed().thenComparing(Variable::getProfit));
    }
}

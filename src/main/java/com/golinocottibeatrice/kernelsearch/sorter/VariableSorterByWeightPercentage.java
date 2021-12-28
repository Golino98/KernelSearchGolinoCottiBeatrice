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

        //TODO Decidere quale implementare in base alle soluzioni delle altre istanze
        //N-N
        //variables.sort(valueComparator.thenComparing(Variable::getProfit));

        //N-R
        //variables.sort(valueComparator.thenComparing(Variable::getProfit).reversed());

        //R-N
        //variables.sort(valueComparator.reversed().thenComparing(Variable::getProfit));

        //R-R
        variables.sort(valueComparator.reversed().thenComparing(Variable::getProfit).reversed());
    }
}

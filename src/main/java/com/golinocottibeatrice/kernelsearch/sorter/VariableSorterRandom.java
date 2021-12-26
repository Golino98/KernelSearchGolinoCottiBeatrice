package com.golinocottibeatrice.kernelsearch.sorter;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.Collections;
import java.util.List;

public class VariableSorterRandom implements VariableSorter{
    @Override
    public void sort(List<Variable> variables) {
        Collections.shuffle(variables);
    }
}

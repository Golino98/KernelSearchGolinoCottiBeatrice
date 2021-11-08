package com.golino.cotti.classes;

import com.golino.cotti.classes.solver.Variable;

import java.util.List;

public class KernelBuilderPositive implements KernelBuilder {
    @Override
    public Kernel build(List<Variable> variables, Configuration config) {
        Kernel kernel = new Kernel();

        for (var v : variables) {
            if (v.getValue() > 0) {
                kernel.addItem(v);
            }
        }
        return kernel;
    }
}

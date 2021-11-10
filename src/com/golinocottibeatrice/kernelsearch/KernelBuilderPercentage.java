package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public class KernelBuilderPercentage implements KernelBuilder {
    @Override
    public Kernel build(List<Variable> variables, Configuration config) {

        Kernel kernel = new Kernel();
        for (var v:variables) {
            if (kernel.size() < Math.round(config.getKernelSize() * variables.size())) {
                kernel.addItem(v);
            }
        }
        return kernel;
    }
}
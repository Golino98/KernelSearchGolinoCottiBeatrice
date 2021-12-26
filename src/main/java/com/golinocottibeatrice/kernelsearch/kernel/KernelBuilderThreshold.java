package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public class KernelBuilderThreshold implements KernelBuilder {

    @Override
    public Kernel build(List<Variable> variables, SearchConfiguration config) {
        Kernel kernel = new Kernel();

        for (var v : variables) {
            if (v.getValue() > 0.6) {
                kernel.addItem(v);
            } else {
                break;
            }
        }
        return kernel;
    }
}



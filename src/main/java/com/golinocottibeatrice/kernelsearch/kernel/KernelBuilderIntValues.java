package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public class KernelBuilderIntValues implements KernelBuilder{
    @Override
    public Kernel build(List<Variable> variables, SearchConfiguration config) {
        Kernel kernel = new Kernel();

        for (var v : variables) {
            if (v.getValue() >= 1) {
                kernel.addItem(v);
            }else{
                break;
            }
        }
        return kernel;
    }
}

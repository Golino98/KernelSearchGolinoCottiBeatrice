package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public class KernelBuilderIntValues extends KernelBuilder{

    @Override
    Kernel fill_kernel(Kernel kernel, List<Variable> variables, SearchConfiguration config) {
        for (var v : variables) {
            if (v.getValue() == 1) {
                kernel.addItem(v);
            }else{
                break;
            }
        }
        return kernel;
    }
}

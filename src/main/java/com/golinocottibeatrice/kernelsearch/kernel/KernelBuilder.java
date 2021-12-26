package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public interface KernelBuilder {
    Kernel build(List<Variable> variables, SearchConfiguration config);
}
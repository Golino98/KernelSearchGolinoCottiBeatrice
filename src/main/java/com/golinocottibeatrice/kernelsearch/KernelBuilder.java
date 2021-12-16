package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public interface KernelBuilder {
    Kernel build(List<Variable> variables, SearchConfiguration config);
}
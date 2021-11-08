package com.golino.cotti.classes;

import com.golino.cotti.classes.solver.Variable;

import java.util.List;

public interface KernelBuilder {
    Kernel build(List<Variable> variables, Configuration config);
}
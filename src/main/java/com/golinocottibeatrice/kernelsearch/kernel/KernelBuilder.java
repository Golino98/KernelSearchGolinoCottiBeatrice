package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public interface KernelBuilder {
    /**
     * Costruisce il kernel set.
     *
     * @param variables lista di variabili.
     * @param config    configurazione della kernel search.
     * @return kernel set creato.
     */
    Kernel build(List<Variable> variables, SearchConfiguration config);
}
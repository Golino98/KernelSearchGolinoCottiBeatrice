package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import javax.swing.*;
import java.util.List;

public abstract class KernelBuilder {
    /**
     * Costruisce il kernel set.
     *
     * @param variables lista di variabili.
     * @param config    configurazione della kernel search.
     * @return kernel set creato.
     */
    public Kernel build(List<Variable> variables, SearchConfiguration config){
        Kernel kernel;
        if (config.getMapImplementation()){
            kernel = new KernelMap();
        }
        else {
            kernel = new Kernel();
        }

        return this.fill_kernel(kernel, variables, config);
    }

    abstract Kernel fill_kernel(Kernel kernel, List<Variable> variables, SearchConfiguration config);
}
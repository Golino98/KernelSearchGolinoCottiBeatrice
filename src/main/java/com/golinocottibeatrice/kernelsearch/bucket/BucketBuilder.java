package com.golinocottibeatrice.kernelsearch.bucket;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.kernel.KernelBuilderPercentage;
import com.golinocottibeatrice.kernelsearch.kernel.KernelBuilderPositive;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public interface BucketBuilder {
    /**
     * Semplice metodo, definito per tutte le classi che faranno {@code implements} dell'interface.
     * In questo caso le classi che fan {@code implements} sono {@link KernelBuilderPositive} e {@link KernelBuilderPercentage}
     *
     * @param variables   lista di variabili
     * @param config, variabile di tipo {@link SearchConfiguration}, usata per settare la configurazione del problema
     * @return una {@link List<Bucket>}
     */
    List<Bucket> build(List<Variable> variables, SearchConfiguration config);
}
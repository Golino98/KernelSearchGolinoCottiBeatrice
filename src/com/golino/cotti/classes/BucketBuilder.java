package com.golino.cotti.classes;

import com.golino.cotti.classes.solver.Variable;

import java.util.List;

public interface BucketBuilder {
    /**
     * Semplice metodo, definito per tutte le classi che faranno <code>implements</code> dell'@interface {@link BucketBuilder}
     * In questo caso le classi che fan <code>implements</code> sono {@link KernelBuilderPositive} e {@link KernelBuilderPercentage}
     *
     * @param variables   lista di variabili
     * @param config, variabile di tipo {@link Configuration}, usata per settare la configurazione del problema
     * @return una {@link List<Bucket>}
     */
    List<Bucket> build(List<Variable> variables, Configuration config);
}
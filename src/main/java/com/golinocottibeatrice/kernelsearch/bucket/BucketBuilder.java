package com.golinocottibeatrice.kernelsearch.bucket;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public interface BucketBuilder {
    /**
     * Costruisce la lista di bucket.
     *
     * @param variables lista di variabili.
     * @param config    configurazione della kernel search.
     * @return lista di bucket creata.
     */
    List<Bucket> build(List<Variable> variables, SearchConfiguration config);
}
package com.golinocottibeatrice.kernelsearch.bucket;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una bucket della kernel search.
 */
public class Bucket {
    private final List<Variable> bucket = new ArrayList<>();

    /**
     * Aggiunge una nuova variabile al bucket.
     *
     * @param v La variabile da aggiungere al bucket.
     */
    public void addVariable(Variable v) {
        bucket.add(v);
    }

    public int size() {
        return bucket.size();
    }

    public List<Variable> getVariables() {
        return bucket;
    }

    /**
     * Verifica se una variabile è contenuta nel bucket.
     *
     * @param v La variabile di cui verificare la presenza.
     * @return {@code true} se la variabile è nel bucket, {@code false} altrimenti.
     */
    public boolean contains(Variable v) {
        return bucket.stream().anyMatch(v::equals);
    }
}


package com.golinocottibeatrice.kernelsearch.bucket;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private final List<Variable> variables = new ArrayList<>();

    public void addVariable(Variable v) {
        v.setFromBucket(true);
        variables.add(v);
    }

    public int size() {
        return variables.size();
    }

    public List<Variable> getVariables() {
        return variables;
    }

    /**
     * @param v variabile di tipo {@link Variable} di cui controllare l'esistenza all'interno della lista variables.
     *          Il controllo viene fatto in maniera case insensitive in modo tale da non avere problemi con le maiuscole.
     * @return {@code true} se la variabile v è presente all'interno della lista, in caso contrario <code>false</code>
     */
    public boolean contains(Variable v) {
        return variables.stream().anyMatch(v2 -> v2.getName().equalsIgnoreCase(v.getName()));
    }
}


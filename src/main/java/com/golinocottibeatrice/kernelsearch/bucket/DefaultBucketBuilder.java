package com.golinocottibeatrice.kernelsearch.bucket;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

public class DefaultBucketBuilder implements BucketBuilder {
    /**
     * @param variables  lista di variabili
     * @param config variabile di tipo {@link SearchConfiguration}, usata per settare la configurazione del problema
     * @return una {@link List<Bucket>}
     */
    @Override
    public List<Bucket> build(List<Variable> variables, SearchConfiguration config) {
        List<Bucket> buckets = new ArrayList<>();
        Bucket b = new Bucket();

        //Il comando floor esegue un arrotondamento all'intero più vicino
        int size = (int) Math.floor(variables.size() * config.getBucketSize());

        for (Variable v : variables) {
            b.addVariable(v);

            if (b.size() == size) {
                buckets.add(b);
                b = new Bucket();
            }
        }
        if (b.size() <= size && b.size() > 0) {
            buckets.add(b);
        }
        return buckets;
    }
}
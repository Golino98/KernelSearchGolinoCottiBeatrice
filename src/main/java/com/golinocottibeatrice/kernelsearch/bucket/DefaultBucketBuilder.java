package com.golinocottibeatrice.kernelsearch.bucket;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

public class DefaultBucketBuilder implements BucketBuilder {
    @Override
    public List<Bucket> build(List<Variable> variables, SearchConfiguration config) {
        var buckets = new ArrayList<Bucket>();

        //Il comando floor esegue un arrotondamento all'intero più vicino
        var size = (int) Math.floor(variables.size() * config.getBucketSize());

        var b = new Bucket();
        for (var v : variables) {
            b.addVariable(v);

            if (b.size() == size) {
                buckets.add(b);
                b = new Bucket();
            }
        }
        // Aggiunge bucket residuo, con meno elementi della size prevista
        buckets.add(b);

        return buckets;
    }
}
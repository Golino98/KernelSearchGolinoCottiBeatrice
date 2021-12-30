package com.golinocottibeatrice.kernelsearch.bucket;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

public class OverlapBucketBuilder implements BucketBuilder{
    @Override
    public List<Bucket> build(List<Variable> variables, SearchConfiguration config) {
        double overlap_ratio = 0.15;
        List<Bucket> buckets = new ArrayList<>();
        Bucket curr_b = new Bucket();
        Bucket prev_b;
        Variable curr_var;

        //Il comando floor esegue un arrotondamento all'intero più vicino
        int size = (int) Math.floor(variables.size() * config.getBucketSize());

        // Bucket 0
        for (int i=0; i<size; i++) {
            curr_var = variables.get(i);
        }

        for (Variable v : variables) {
            curr_b.addVariable(v);

            if (curr_b.size() == size) {
                buckets.add(curr_b);
                curr_b = new Bucket();
            }
        }
        if (curr_b.size() <= size && curr_b.size() > 0) {
            buckets.add(curr_b);
        }
        return buckets;
    }
}

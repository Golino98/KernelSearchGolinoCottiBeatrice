package com.golinocottibeatrice.kernelsearch.bucket;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

public class OverlapBucketBuilder implements BucketBuilder{
    @Override
    public List<Bucket> build(List<Variable> variables, SearchConfiguration config) {
        double overlap_ratio = config.getOverlapRatio();
        List<Bucket> buckets = new ArrayList<>();
        Bucket curr_b = new Bucket();
        Bucket prev_b;
        Variable curr_var;

        //Il comando floor esegue un arrotondamento all'intero più vicino
        int size = (int) Math.floor(variables.size() * config.getBucketSize());
        int overlappingVars = (int) Math.floor(size * overlap_ratio);

        // Bucket 0 (no overlap)
        for (int i=0; i<size; i++){
            curr_b.addVariable(variables.remove(0));
        }
        prev_b = curr_b;
        buckets.add(curr_b);

        // All middle buckets
        int curr_size = variables.size();
        while (curr_size > size) {
            curr_b = new Bucket();
            for (int i=0; i<size; i++) {
                if (i<overlappingVars) {
                    // Add overlapping variables from previous bucket
                    curr_var = prev_b.getVariables().get(size-overlappingVars+i);
                    curr_b.addVariable(curr_var);
                } else {
                    curr_b.addVariable(variables.remove(0));
                }
            }
            curr_size = variables.size();
            prev_b = curr_b;
            buckets.add(curr_b);
        }

        // Last bucket
        curr_b = new Bucket();
        for (int i=size-overlappingVars; i<size; i++) {
            curr_var = prev_b.getVariables().get(i);
            curr_b.addVariable(curr_var);
        }
        while (variables.size()>0) {
            curr_b.addVariable(variables.remove(0));
        }
        buckets.add(curr_b);

        return buckets;
    }
}

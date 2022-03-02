package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.bucket.BucketBuilder;
import com.golinocottibeatrice.kernelsearch.bucket.DefaultBucketBuilder;
import com.golinocottibeatrice.kernelsearch.bucket.OverlapBucketBuilder;
import com.golinocottibeatrice.kernelsearch.kernel.*;
import com.golinocottibeatrice.kernelsearch.sorter.*;

public class DependenciesFactory {
    private static final String UNRECOGNIZED_KERNEL_BUILDER = "Unrecognized kernel builder.";
    private static final String UNRECOGNIZED_BUCKET_BUILDER = "Unrecognized bucket builder.";
    private static final String UNRECOGNIZED_VAR_SORTER = "Unrecognized item sorter.";

    public static KernelBuilder getKernelBuilder(int type) {
        return switch (type) {
            case 0 -> new KernelBuilderPositive();
            case 1 -> new KernelBuilderPercentage();
            case 2 -> new KernelBuilderIntValues();
            default -> throw new IllegalStateException(UNRECOGNIZED_KERNEL_BUILDER);
        };
    }

    public static BucketBuilder getBucketBuilder(int type) {
        return switch (type) {
            case 0 -> new DefaultBucketBuilder();
            case 1 -> new OverlapBucketBuilder();
            default -> throw new IllegalStateException(UNRECOGNIZED_BUCKET_BUILDER);
        };

    }

    public static VariableSorter getVariableSorter(int type) {
        return switch (type) {
            case 0 -> new VariableSorterByValueAndAbsoluteRC();
            case 1 -> new VariableSorterByAbsoluteRCAndValue();
            case 2 -> new VariableSorterByProfitDivideWeight();
            case 3 -> new VariableSorterRandom();
            case 4 -> new VariableSorterByValueProfitWeightAndRC();
            default -> throw new IllegalStateException(UNRECOGNIZED_VAR_SORTER);
        };
    }
}

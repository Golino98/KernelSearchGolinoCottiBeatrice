package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Item;

/**
 * Rappresenta una variabile x(i,j) del problema.
 */
public class Variable {
    private final String name;
    private final Item item;
    private final int knapsackCapacity;
    private int timesUsed = 0;
    private int initialSolutionCount; // Eject procedure
    private double value;
    private double rc;
    private Boolean fromBucket=true; // true -> variable is in a bucket
                                     // false -> variable was in the kernel from the start

    Variable(String name, Item item, int knapCapacity) {
        this.name = name;
        this.item = item;
        this.knapsackCapacity = knapCapacity;
    }

    public Boolean isFromBucket() {
        return fromBucket;
    }

    public void setFromBucket(Boolean fromBucket) {
        this.fromBucket = fromBucket;
    }

    public void resetTimesUsed(int solutionsCount) {
        this.initialSolutionCount = solutionsCount;
        this.timesUsed = 0;
    }

    private int getTimesUsed() {
        return this.initialSolutionCount + this.timesUsed;
    }

    public void increaseTimesUsed() {
        this.timesUsed++;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public int getWeight() {
        return item.getWeight();
    }

    public int getProfit() {
        return item.getProfit();
    }

    public double getRC() {
        return rc;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public int getKnapsackCapacity() {
        return knapsackCapacity;
    }

    public String toString() {
        return name;
    }

    public boolean exceedsThreshold(int threshold, int solutions_count) {
        return (solutions_count-this.getTimesUsed()) - this.getTimesUsed() < threshold;
    }
}

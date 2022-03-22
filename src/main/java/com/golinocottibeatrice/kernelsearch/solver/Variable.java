package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.instance.Knapsack;

import java.util.Objects;

/**
 * Rappresenta una variabile x(i,j) del problema.
 */
public class Variable {
    private final String name;
    private final Item item;
    private final Knapsack knapsack;
    private int timesUsed = 0;
    private int initialSolutionCount; // Eject procedure
    private double value;
    private double rc;
    private Boolean fromBucket=true; // true -> variable is in a bucket
                                     // false -> variable was in the kernel from the start

    public Variable(String name, Item item, Knapsack knapsack) {
        this.name = name;
        this.item = item;
        this.knapsack = knapsack;
    }

    public String getName() {
        return name;
    }

    public int getProfit() {
        return item.getProfit();
    }

    public int getWeight() {
        return item.getWeight();
    }

    public int getKnapsackCapacity() {
        return knapsack.getCapacity();
    }

    public void resetTimesUsed(int solutionsCount) {
        this.initialSolutionCount = solutionsCount;
        this.timesUsed = 0;
    }

    private int getTimesUsed() {
        return this.initialSolutionCount + this.timesUsed;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getRC() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public void resetTimesUsed() {
        this.timesUsed = 0;
    }

    public void increaseTimesUsed() {
        this.timesUsed++;
    }

    public boolean isFromBucket() {
        return fromBucket;
    }

    public void setFromBucket(boolean fromBucket) {
        this.fromBucket = fromBucket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return name;
    }

    /**
     * Checks if this variable exceeds the given threshold. If false it should be removed (if the eject option is active)
     * @param threshold the given threshold to verify
     * @param solutions_count the total number of solutions visited
     * @return True -> the variable respects the threshold | False -> the variable should be removed
     */
    public boolean respectsThreshold(int threshold, int solutions_count) {
        int times_not_used = solutions_count-this.getTimesUsed();
        return times_not_used - this.getTimesUsed() < threshold;
    }
}

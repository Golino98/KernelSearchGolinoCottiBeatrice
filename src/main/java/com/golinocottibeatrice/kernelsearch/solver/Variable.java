package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.instance.Knapsack;

/**
 * Rappresenta una variabile x(i,j) del problema.
 */
public class Variable {
    private final String name;
    private final Item item;
    private final Knapsack knapsack;
    private int timesUsed = 0;
    private double value;
    private double rc;
    private Boolean fromBucket=true;

    Variable(String name, Item item, Knapsack knapsack) {
        this.name = name;
        this.item = item;
        this.knapsack = knapsack;
    }

    public Boolean isFromBucket() {
        return fromBucket;
    }

    public void setFromBucket(Boolean fromBucket) {
        this.fromBucket = fromBucket;
    }

    public void resetTimesUsed() {
        this.timesUsed = 0;
    }

    public int getTimesUsed() {
        return this.timesUsed;
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
        return knapsack.getCapacity();
    }
}

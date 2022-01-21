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
    private boolean fromBucket = true;

    public Variable(String name, Item item, Knapsack knapsack) {
        this.name = name;
        this.item = item;
        this.knapsack = knapsack;
    }

    public Variable(String name, Item item, Knapsack knapsack, double value) {
        this(name, item, knapsack);
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return item.getWeight();
    }

    public int getProfit() {
        return item.getProfit();
    }

    public int getKnapsackCapacity() {
        return knapsack.getCapacity();
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

    public int getTimesUsed() {
        return this.timesUsed;
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
}

package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Item;

/**
 * Rappresenta una variabile x(i,j) del problema.
 */
public class Variable {
    private final String name;
    private final Item item;
    private final int knapsackCapacity;
    private double value;
    private double rc;

    Variable(String name, Item item, int knapCapacity) {
        this.name = name;
        this.item = item;
        this.knapsackCapacity = knapCapacity;
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
}

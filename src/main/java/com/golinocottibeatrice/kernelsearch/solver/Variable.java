package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Item;

/**
 * Rappresenta una variabile x(i,j) del problema.
 */
public class Variable {
    private final String name;
    private final Item item;
    private double value;
    private double rc;

    Variable(String name, Item item) {
        this.name = name;
        this.item = item;
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

    public double getProfitDividedByWeight() {
        return ((double) getProfit()) / ((double) getWeight());
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }
}

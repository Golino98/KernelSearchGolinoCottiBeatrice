package com.golinocottibeatrice.kernelsearch.instance;

public class Knapsack {
    private final int index;
    private final int capacity;

    public Knapsack(int index, int capacity) {
        this.index = index;
        this.capacity = capacity;
    }

    public int getIndex() {
        return index;
    }

    public int getCapacity() {
        return capacity;
    }
}

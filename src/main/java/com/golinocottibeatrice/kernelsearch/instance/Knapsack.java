package com.golinocottibeatrice.kernelsearch.instance;

/**
 * Rappresenta un knapsack dell'istanza del problema.
 * Un knapsack è caratterizzato da un indice (ordine in cui è stato letto) e una capacità.
 */
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

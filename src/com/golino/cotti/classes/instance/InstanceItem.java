package com.golino.cotti.classes.instance;

/**
 * Rappresenta un oggetto dell'istanza del problema.
 * Un oggetto è caratterizzato da un peso e da un profitto.
 */
public class InstanceItem {
    private final int weight;
    private final int profit;

    public InstanceItem(int weight, int profit) {
        this.weight = weight;
        this.profit = profit;
    }

    public int getWeight() {
        return weight;
    }

    public int getProfit() {
        return profit;
    }
}

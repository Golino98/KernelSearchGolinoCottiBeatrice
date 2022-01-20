package com.golinocottibeatrice.kernelsearch.instance;

/**
 * Rappresenta un oggetto dell'istanza del problema.
 * Un oggetto è caratterizzato da un peso e da un profitto.
 * NON contiene informazioni riguardanti la risoluzione del problema, come
 * il coefficiente di costo ridotto.
 */
public class Item {
    private final int index;
    private final int weight;
    private final int profit;

    Item(int index, int weight, int profit) {
        this.index = index;
        this.weight = weight;
        this.profit = profit;
    }

    public int getIndex() {
        return index;
    }

    public int getWeight() {
        return weight;
    }

    public int getProfit() {
        return profit;
    }

}

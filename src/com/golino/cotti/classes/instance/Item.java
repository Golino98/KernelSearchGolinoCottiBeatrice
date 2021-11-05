package com.golino.cotti.classes.instance;

public class Item {
    private final int weight;
    private final int profit;

    public Item(int weight, int profit) {
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

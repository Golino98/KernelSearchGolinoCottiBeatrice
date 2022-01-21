package com.golinocottibeatrice.kernelsearch.additions;

import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.instance.Knapsack;

import java.util.*;

public class BinPacking {
    private final List<Knapsack> knapsacks;
    private final List<Item> items;

    public BinPacking(List<Knapsack> knapsacks, List<Item> items) {
        this.knapsacks = knapsacks;
        this.items = items;
    }

    public ItemsPacking pack() {
        var packing = new ItemsPacking();
        items.sort(Comparator.comparing(Item::getWeight).reversed());

        for (var i : items) {
            var found = false;
            for (var k : knapsacks) {
                if (packing.getResidualCapacity(k) >= i.getWeight()) {
                    found = true;
                    packing.pack(k, i);
                }
            }
            if (!found) {
                return new ItemsPacking();
            }
        }

        return packing;
    }
}

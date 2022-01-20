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

    public Packing pack() {
        var packing = new Packing();
        items.sort(Comparator.comparing(Item::getWeight).reversed());

        for (var i : items) {
            for (var k : knapsacks) {
                packing.add(k);
                if (packing.getResidualCapacity(k) >= i.getWeight()) {
                    packing.pack(i, k);
                }
            }
        }

        return packing;
    }

}

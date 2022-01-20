package com.golinocottibeatrice.kernelsearch.reduction;

import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.instance.Knapsack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Packing {
    private final Map<Knapsack, List<Item>> packing = new HashMap<>();

    public void add(Knapsack k) {
        if (!packing.containsKey(k)) {
            packing.put(k, new ArrayList<>());
        }
    }

    public void pack(Item i, Knapsack k) {
        packing.get(k).add(i);
    }

    public int getResidualCapacity(Knapsack k) {
        return k.getCapacity() - packing.get(k).stream().mapToInt(Item::getWeight).sum();
    }

    public boolean isEmpty() {
        return packing.isEmpty();
    }

    public void putAll(Packing p) {
        packing.putAll(p.packing);
    }
}

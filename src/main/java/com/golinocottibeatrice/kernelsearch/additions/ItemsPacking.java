package com.golinocottibeatrice.kernelsearch.additions;

import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.instance.Knapsack;

import java.util.*;

/**
 * Rappresenta un packing di item nei knapsack.
 */
public class ItemsPacking {
    // Ad ogni knapsack è associata una lista di item, eventualmente vuota
    private final Map<Knapsack, List<Item>> packing = new HashMap<>();

    public void pack(Knapsack k, Item i) {
        initKnapsack(k);
        packing.get(k).add(i);
    }

    public int getResidualCapacity(Knapsack k) {
        initKnapsack(k);
        return k.getCapacity() - packing.get(k).stream().mapToInt(Item::getWeight).sum();
    }

    public boolean isEmpty() {
        // Se anche solo una delle liste di item presenti nel packing
        // contiene item, restituisci false per indicare che la lista
        // non è vuota.
        for (var l : packing.values()) {
            if (!l.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void putAll(ItemsPacking p) {
        if (p.isEmpty()) {
            return;
        }
        packing.putAll(p.packing);
    }

    public Set<Knapsack> keySet() {
        return packing.keySet();
    }

    public List<Item> get(Knapsack k) {
        return packing.get(k);
    }

    private void initKnapsack(Knapsack k) {
        if (!packing.containsKey(k)) {
            packing.put(k, new ArrayList<>());
        }
    }
}

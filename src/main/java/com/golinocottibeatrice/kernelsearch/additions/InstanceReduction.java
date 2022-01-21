package com.golinocottibeatrice.kernelsearch.additions;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.instance.Knapsack;

import java.util.*;

public class InstanceReduction {
    private final List<Knapsack> knapsacks;
    private final List<Item> items;

    public InstanceReduction(Instance instance) {
        this.knapsacks = instance.getKnapsacks().stream().sorted(Comparator.comparing(Knapsack::getCapacity)).toList();
        this.items = new ArrayList<>(instance.getItems());
    }

    public ItemsPacking reduce() {
        var fullPacking = new ItemsPacking();
        var I = new LinkedList<Knapsack>();

        for (var i : knapsacks) {
            I.add(i);

            var J = new LinkedList<Item>();
            for (var j : items) {
                if (j.getWeight() <= Collections.max(I.stream().map(Knapsack::getCapacity).toList())) {
                    J.add(j);
                }
            }

            if (J.stream().mapToInt(Item::getWeight).sum() <= I.stream().mapToInt(Knapsack::getCapacity).sum()) {
                var packing = new BinPacking(I, J).pack();
                if (!packing.isEmpty()) {
                    I = new LinkedList<>();
                    items.removeAll(J);

                    fullPacking.putAll(packing);
                }
            }
        }

        return fullPacking;
    }
}

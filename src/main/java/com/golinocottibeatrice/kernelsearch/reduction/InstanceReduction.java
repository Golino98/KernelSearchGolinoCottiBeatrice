package com.golinocottibeatrice.kernelsearch.reduction;

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

    public void reduce() {
        var packing = new HashMap<Knapsack, List<Item>>();
        var I = new LinkedList<Knapsack>();
        var sizeI = 0;

        for (var i : knapsacks) {
            I.add(i);
            sizeI += i.getCapacity();

            var J = new LinkedList<Item>();
            var sizeJ = 0;
            for (var j : items) {
                if (j.getWeight() <= Collections.max(I.stream().map(Knapsack::getCapacity).toList())) {
                    J.add(j);
                    sizeJ += j.getWeight();
                }
            }

            if (sizeJ <= sizeI) {
                var newPacking = new BinPacking(I, J).pack();
                if (!newPacking.isEmpty()) {
                    I = new LinkedList<>();
                    items.removeAll(J);
                }
            }
        }

    }
}

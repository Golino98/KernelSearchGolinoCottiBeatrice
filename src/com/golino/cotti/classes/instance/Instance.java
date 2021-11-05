package com.golino.cotti.classes.instance;

import java.util.Collections;
import java.util.List;

public class Instance {
    private final List<Integer> capacities;
    private final List<Item> items;

    public Instance(List<Integer> capacities, List<Item> items) {
        this.capacities = capacities;
        this.items = items;
    }

    public int getNumKnapsacks() {
        return capacities.size();
    }

    public int getNumItems() {
        return items.size();
    }

    public List<Integer> getCapacities() {
        return Collections.unmodifiableList(capacities);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }
}

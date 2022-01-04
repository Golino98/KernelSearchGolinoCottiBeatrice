package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Creatore della dominance list dell'istanza.
 */
public class DominanceListBuilder {
    private final Instance instance;

    public DominanceListBuilder(Instance instance) {
        this.instance = instance;
    }

    public List<Pair> build() {
        var dominanceList = new ArrayList<Pair>();

        // Ordina le variabili come spiegato nel paper
        var items = instance.getItems();
        items.sort(Comparator.comparing(Item::getWeight).reversed().thenComparing(Item::getProfit));

        for (var k = 0; k < instance.getNumItems() - 1; k++) {
            for (var j = k + 1; j < instance.getNumItems(); j++) {
                if (items.get(j).getProfit() >= items.get(k).getProfit()) {
                    dominanceList.add(new Pair(j, k));
                }
            }
        }

        return dominanceList;
    }
}

package com.golinocottibeatrice.kernelsearch.additions;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.instance.Item;

import java.util.Comparator;

/**
 * Creatore della dominance list dell'istanza.
 */
public class DominanceListBuilder {
    private final Instance instance;

    public DominanceListBuilder(Instance instance) {
        this.instance = instance;
    }

    public DominanceList build() {
        var dominanceList = new DominanceList();

        // Ordina le variabili come spiegato nel paper
        var items = instance.getItems()
                .stream()
                .sorted(Comparator.comparing(Item::getWeight).reversed().thenComparing(Item::getProfit))
                .toList();

        for (var k = 0; k < items.size() - 1; k++) {
            for (var j = k + 1; j < items.size(); j++) {
                if (items.get(j).getProfit() >= items.get(k).getProfit()) {
                    dominanceList.add(items.get(j), items.get(k));
                }
            }
        }

        return dominanceList;
    }
}

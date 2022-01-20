package com.golinocottibeatrice.kernelsearch.dominance;

import com.golinocottibeatrice.kernelsearch.instance.Item;

/**
 * Rappresenta una coppia di valori della dominance list.
 */
public class DominancePair {
    private final Item j;
    private final Item k;

    public DominancePair(Item j, Item k) {
        this.j = j;
        this.k = k;
    }

    public Item getJ() {
        return j;
    }

    public Item getK() {
        return k;
    }
}


package com.golino.cotti.classes;

import java.util.ArrayList;
import java.util.List;

public class DefaultBucketBuilder implements BucketBuilder {
    /**
     * @param items  lista di Item
     * @param config variabile di tipo {@link Configuration}, usata per settare la configurazione del problema
     * @return una {@link List<Bucket>}
     */
    @Override
    public List<Bucket> build(List<Item> items, Configuration config) {
        List<Bucket> buckets = new ArrayList<>();
        Bucket b = new Bucket();

        //Il comando floor esegue un arrotondamento all'intero più vicino
        int size = (int) Math.floor(items.size() * config.getBucketSize());
        for (Item it : items) {
            b.addItem(it);

            if (b.size() == size) {
                buckets.add(b);
                b = new Bucket();
            }
        }
        if (b.size() < size && b.size() > 0) {
            buckets.add(b);
        }
        return buckets;
    }
}
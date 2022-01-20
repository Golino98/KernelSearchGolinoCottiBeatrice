package com.golinocottibeatrice.kernelsearch.dominance;

import com.golinocottibeatrice.kernelsearch.instance.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DominanceList implements Iterable<DominancePair> {
    private final List<DominancePair> dominanceList = new ArrayList<>();

    public void add(Item j ,Item k) {
        dominanceList.add(new DominancePair(j,k));
    }


    @Override
    public Iterator<DominancePair> iterator() {
        return dominanceList.iterator();
    }

    @Override
    public void forEach(Consumer<? super DominancePair> action) {
        dominanceList.forEach(action);
    }

    @Override
    public Spliterator<DominancePair> spliterator() {
        return dominanceList.spliterator();
    }
}

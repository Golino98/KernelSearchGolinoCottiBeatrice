package com.golino.cotti.classes;

import java.util.Comparator;
import java.util.List;

public class ItemSorterByValueAndAbsoluteRC implements ItemSorter {
    /**
     * Questo metodo prende una lista di items, una volta presi, utilizzando la classe Comparator, fa il confronto inverso fra tutti
     * gli Item basandosi sul valore del xr, il secondo valore utilizzato per la comparazione è l'rc
     *
     * @param items, lista di item su cui fare un'operazione di sorting
     */
    @Override
    public void sort(List<Item> items) {
        items.sort(Comparator.comparing(Item::getXr).reversed().thenComparing(Item::getRc));
    }
}

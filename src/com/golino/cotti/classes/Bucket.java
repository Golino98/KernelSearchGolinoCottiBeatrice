package com.golino.cotti.classes;

import java.util.ArrayList;
import java.util.List;

public class Bucket {

    private List<Item> items;

    /**
     * Metodo costruttore per {@link Bucket}
     */
    public Bucket() {
        items = new ArrayList<>();
    }

    /**
     * Metodo che permette di aggiungere un {@link Item} alla lista items
     *
     * @param it parametro di tipo {@link Item} da aggiungere alla lista
     */
    public void addItem(Item it) {
        items.add(it);
    }

    /**
     * @return lunghezza della List<{@link Item}> items
     */
    public int size() {
        return items.size();
    }

    /**
     * @return di items, ovvero una List<{@link Item}>
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Modifica da far controllare alla profe
     *
     * @param it variabile di tipo {@link Item} di cui controllare l'esistenza all'interno della lista items.
     *           Il controllo viene fatto in maniera case Insensitive in modo tale da non avere problemi con le maiuscole.
     * @return <code>true</code> se l'item it è presente all'interno della lista items, in caso contrario <code>false</code>
     */
    public boolean contains(Item it) {
        return items.stream().anyMatch(it2 -> it2.getName().equalsIgnoreCase(it.getName()));
    }

    /**
     * @param it1 parametro da andare a rimuovere all'interno della lista.
     *            Il controllo viene effettuato tramite il nome in maniera case insensitive, in modo tale da non avere problemi con le maiuscole
     */
    public void removeItem(Item it1) {
        for (int i = 0; i < items.size(); i++) {
            Item it2 = items.get(i);
            if (it2.getName().equalsIgnoreCase(it1.getName())) {
                items.remove(it2);
                break;
            }
        }
    }
}
package com.golino.cotti.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il kernel set del problema.
 */
public class Kernel {
    private final List<Item> items;

    /**
     * Crea un nuovo kernel set.
     *
     * @param items Gli item da includere nel kernel set.
     */
    public Kernel(List<Item> items) {
        this.items = items;
    }

    /**
     * Crea un nuovo kernel set vuoto.
     */
    public Kernel() {
        this.items = new ArrayList<>();
    }

    /**
     * Aggiunge un {@link Item} alla lista.
     *
     * @param it {@link Item} da aggiungere alla lista.
     */
    public void addItem(Item it) {
        items.add(it);
    }

    /**
     * Verifica la presenza di un {@link Item} all'interno della lista.
     *
     * @param it {@link Item} di cui verificare la presenza all'interno della lista.
     * @return <code>true</code> se viene trovato un qualunque tipo di match tramite nome (in maniera case insensitive
     * fra il nostro item it e la lista items), <code>false</code> altrimenti.
     */
    public boolean contains(Item it) {
        return items.stream().anyMatch(it2 -> it2.getName().equalsIgnoreCase(it.getName()));
    }

    /**
     * @return la dimensione della {@link List<Item>} di item.
     */
    public int size() {
        return items.size();
    }
}
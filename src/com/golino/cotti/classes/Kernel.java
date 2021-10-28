package com.golino.cotti.classes;

import java.util.ArrayList;
import java.util.List;

public class Kernel {
    private final List<Item> items;

    /**
     * Metodo costruttore della classe {@link Kernel}
     *
     * @param items
     */
    public Kernel(List<Item> items) {
        this.items = items;
    }

    /**
     * Metoco costruttore per la classe {@link Kernel} che non richiede parametri in ingersso
     * Definisce in maniera automatica la {@link List<Item>} come un {@link ArrayList}
     */
    public Kernel() {
        this.items = new ArrayList<>();
    }

    /**
     * Metodo che permette di aggiungere un {@link Item} alla lista
     *
     * @param it {@link Item} da aggiungere alla lista
     */
    public void addItem(Item it) {
        items.add(it);
    }

    /**
     * Metodo utilizzato per verificare la presenza di un {@link Item} all'interno della nostra lista
     *
     * @param it {@link Item} di cui verificare la presenza all'interno della lista
     * @return <code>true</code> se viene trovato un qualunque tipo di match tramite nome (in maniera case insensitive
     * fra il nostro item it e la lista items), <code>false</code> altrimenti.
     */
    public boolean contains(Item it) {
        return items.stream().anyMatch(it2 -> it2.getName().equalsIgnoreCase(it.getName()));
    }

    /**
     * @return la dimensione della {@link List<Item>} items
     */
    public int size() {
        return items.size();
    }
}
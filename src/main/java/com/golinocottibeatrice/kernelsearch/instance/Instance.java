package com.golinocottibeatrice.kernelsearch.instance;

import java.util.List;

/**
 * Rappresenta i dati di un'istanza del problema Multiple Knapsack.
 * Non si occupa della creazione dei vincoli e della funzione obiettivo.
 * <p>
 * Ad ogni istanza è associato un nome, che corrisponde al nome del file
 * d'istanza privato dell'estensione.
 */
public class Instance {
    private final String name;
    // Viene usata la List perchè è necessario assegnare una posizione agli item
    // in base all'ordine con cui vengono letti
    private final List<Knapsack> knapsacks;
    private final List<Item> items;

    /**
     * Crea una nuova istanza.
     *
     * @param name      Il nome dell'istanza.
     * @param knapsacks I knapsack dell'istanza.
     * @param items     Gli oggetti dell'istanza.
     */
    Instance(String name, List<Knapsack> knapsacks, List<Item> items) {
        this.name = name;
        this.knapsacks = knapsacks;
        this.items = items;
    }

    public String getName() {
        return name;
    }


    public int getNumKnapsacks() {
        return knapsacks.size();
    }

    public int getNumItems() {
        return items.size();
    }

    public List<Knapsack> getKnapsacks() {
        return knapsacks;
    }

    public List<Item> getItems() {
        return items;
    }
}

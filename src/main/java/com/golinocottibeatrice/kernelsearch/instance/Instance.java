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
    private final List<Integer> capacities;
    private final List<Item> items;

    /**
     * Crea una nuova istanza.
     *
     * @param name       Il nome dell'istanza.
     * @param capacities La capacità dei knapsack dell'istanza.
     * @param items      Gli oggetti dell'istanza.
     */
    Instance(String name, List<Integer> capacities, List<Item> items) {
        this.name = name;
        this.capacities = capacities;
        this.items = items;
    }

    public String getName() {
        return name;
    }


    public int getNumKnapsacks() {
        return capacities.size();
    }

    public int getNumItems() {
        return items.size();
    }

    public int getCapacity(int i) {
        return capacities.get(i);
    }

    public List<Integer> getCapacities() {
        return capacities;
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(int i) {
        return items.get(i);
    }

    public int getWeight(int i) {
        return getItem(i).getWeight();
    }

    public int getProfit(int i) {
        return getItem(i).getProfit();
    }
}

package com.golinocottibeatrice.kernelsearch.instance;

import java.util.Collections;
import java.util.List;

/**
 * Rappresenta i dati di un'istanza del problema Multiple Knapsack.
 * Non si occupa della creazione dei vincoli e della funzione obiettivo.
 */
public class Instance {
    // Viene usata la List perchè è necessario assegnare una posizione agli item
    // in base all'ordine con cui vengono letti
    private final List<Integer> capacities;
    private final List<InstanceItem> items;

    /**
     * Crea una nuova istanza.
     *
     * @param capacities La capacità dei knapsack dell'istanza.
     * @param items      Gli oggetti dell'istanza.
     */
    public Instance(List<Integer> capacities, List<InstanceItem> items) {
        this.capacities = capacities;
        this.items = items;
    }

    public int getNumKnapsacks() {
        return capacities.size();
    }

    public int getNumItems() {
        return items.size();
    }

    public List<Integer> getCapacities() {
        // Restituisce una lista NON modificabile
        return Collections.unmodifiableList(capacities);
    }

    public int getCapacity(int i) {
        return capacities.get(i);
    }

    public List<InstanceItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public InstanceItem getItem(int i) {
        return items.get(i);
    }

    public int getWeight(int i) {
        return getItem(i).getWeight();
    }

    public int getProfit(int i) {
        return getItem(i).getProfit();
    }
}

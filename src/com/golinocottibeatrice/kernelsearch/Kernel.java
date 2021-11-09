package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il kernel set del problema.
 */
public class Kernel {
    private final List<Variable> variables;

    /**
     * Crea un nuovo kernel set.
     *
     * @param variables Le variabili da includere nel kernel set.
     */
    public Kernel(List<Variable> variables) {
        this.variables = variables;
    }

    /**
     * Crea un nuovo kernel set vuoto.
     */
    public Kernel() {
        this.variables = new ArrayList<>();
    }

    /**
     * Aggiunge un {@link Variable} alla lista.
     *
     * @param v {@link Variable} da aggiungere alla lista.
     */
    public void addItem(Variable v) {
        variables.add(v);
    }

    /**
     * Verifica la presenza di un {@link Variable} all'interno della lista.
     *
     * @param v {@link Variable} di cui verificare la presenza all'interno della lista.
     * @return <code>true</code> se viene trovato un qualunque tipo di match tramite nome (in maniera case insensitive
     * fra la variabile v e la lista delle variabili), <code>false</code> altrimenti.
     */
    public boolean contains(Variable v) {
        return variables.stream().anyMatch(v2 -> v2.getName().equalsIgnoreCase(v.getName()));
    }

    /**
     * @return la dimensione della {@link List<Variable>} di variabili.
     */
    public int size() {
        return variables.size();
    }
}
package com.golinocottibeatrice.kernelsearch.search;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il kernel set del problema.
 */
public class Kernel {
    private final List<Variable> variables;

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
     */
    public boolean contains(Variable v) {
        return variables.stream().anyMatch(v2 -> v2.getName().equals(v.getName()));
    }

    /**
     * @return la dimensione della {@link List<Variable>} di variabili.
     */
    public int size() {
        return variables.size();
    }
}
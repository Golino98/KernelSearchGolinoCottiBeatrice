package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.solver.Solution;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Rappresenta il kernel set del problema.
 */
public class Kernel {
    private Map<String, Variable> variables;

    /**
     * Crea un nuovo kernel set vuoto.
     */
    public Kernel() {
        variables = new HashMap<>();
    }

    /**
     * Aggiunge un {@link Variable} alla lista e inizializza il contatore della eject.
     *
     * @param v {@link Variable} da aggiungere alla lista.
     */
    public void addItem(Variable v, int solutionsCount) {
        v.resetTimesUsed(solutionsCount);
        variables.put(v.getName(), v);
    }

    /**
     * Aggiunge un {@link Variable} alla lista.
     *
     * @param v {@link Variable} da aggiungere alla lista.
     */
    public void addItem(Variable v) {
        variables.put(v.getName(), v);
    }

    /**
     * Verifica la presenza di un {@link Variable} all'interno della lista.
     *
     * @param v {@link Variable} di cui verificare la presenza all'interno della lista.
     */
    public boolean contains(Variable v) {
        return this.variables.containsKey(v.getName());
    }

    /**
     * @return la dimensione della {@link List<Variable>} di variabili.
     */
    public int size() {
        return variables.size();
    }

    public List<Variable> getVariables() {
        return this.variables.values().stream().toList();
    }

    /**
     * Updates the 'timesUsed' attribute for the variables in the Kernel
     *
     * @param solution the current solution used for the update
     */
    public void updateUsages(Solution solution) {
        solution.getVariables()
                .stream()
                .filter(v -> v.getValue() >= 1)
                .forEach(v -> this.variables.get(v.getName()).increaseTimesUsed());
    }

    /**
     * Esegue eject procedure. Rimuove variabile se #volte non usata - #volte usata >= threshold
     *
     * @param threshold the threshold defined for the eject procedure
     * @param solutions_count numero di soluzioni create durante esecuzione di questa iterazione
     * @return total of variables removed from the kernel
     */
    public int checkForEject(int threshold, int solutions_count) {
        int initial_count = this.variables.size();
        List<String> tbRemoved = new ArrayList<>();

        this.variables.forEach((v_name, v_value) -> {
            if (v_value.isFromBucket() && v_value.exceedsThreshold(threshold, solutions_count))
                tbRemoved.add(v_name);
        });

        tbRemoved.forEach(v_name -> this.variables.remove(v_name));

        return initial_count - this.variables.size();
    }
}
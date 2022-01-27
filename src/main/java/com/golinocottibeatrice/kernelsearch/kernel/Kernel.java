package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.solver.Solution;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rappresenta il kernel set del problema.
 */
public class Kernel {
    private List<Variable> variables;

    /**
     * Crea un nuovo kernel set vuoto.
     */
    public Kernel() {
        variables = new ArrayList<>();
    }

    /**
     * Aggiunge un {@link Variable} alla lista.
     *
     * @param v {@link Variable} da aggiungere alla lista.
     */
    public void addItem(Variable v) {
        v.resetTimesUsed();
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

    public List<Variable> getVariables() {
        return this.variables;
    }

    /**
     * Updates the 'timesUsed' attribute for the variables in the Kernel
     *
     * @param solution the current solution used for the update
     */
    public void updateUsages(Solution solution) {
        List<Variable> activeInSolution = solution.getVariables().stream()
                .filter(variable -> variable.getValue() > 0).toList();

        this.variables.forEach(variable -> {
            if (activeInSolution.stream().anyMatch(v -> v.getName().equals(variable.getName()))) {
                variable.increaseTimesUsed();
            }
        });
    }

    /**
     * Esegue eject procedure. Rimuove variabile se #volte non usata - #volte usata >= threshold
     *
     * @param threshold      the threshold defined for the eject procedure
     * @param solutionsCount numero di soluzioni create durante esecuzione di questa iterazione
     * @return total of variables removed from the kernel
     */
    public int checkForEject(int threshold, int solutionsCount) {
        var oldSize = variables.size();
        variables.removeIf(v -> v.isFromBucket() && violatesThreshold(solutionsCount, v.getTimesUsed(), threshold));

        return variables.size() - oldSize;
    }

    public void resetUsages() {
        this.variables.forEach(Variable::resetTimesUsed);
    }

    private boolean violatesThreshold(int solutionsCount, int timesUsed, int threshold) {
        var timesNotUsed = solutionsCount - timesUsed;
        return timesNotUsed - timesUsed >= threshold;
    }
}
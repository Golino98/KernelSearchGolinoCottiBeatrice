package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.solver.Solution;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il kernel set del problema.
 */
public class Kernel {
    private final List<Variable> kernel = new ArrayList<>();

    /**
     * Aggiunge una {@link Variable} al kernel.
     *
     * @param v {@link Variable} da aggiungere al kernel.
     */
    public void addItem(Variable v) {
        v.resetTimesUsed();
        kernel.add(v);
    }

    /**
     * Verifica la presenza di un {@link Variable} all'interno della lista.
     *
     * @param v {@link Variable} di cui verificare la presenza all'interno della lista.
     */
    public boolean contains(Variable v) {
        return kernel.stream().anyMatch(v::equals);
    }

    /**
     * @return La dimensione del kernel.
     */
    public int size() {
        return kernel.size();
    }

    public List<Variable> getVariables() {
        return kernel;
    }

    /**
     * Updates the 'timesUsed' attribute for the variables in the Kernel
     *
     * @param solution the current solution used for the update
     */
    public void updateUsages(Solution solution) {
        var activeInSolution = solution.getVariables().stream()
                .filter(v -> v.getValue() > 0).toList();

        kernel.forEach(v -> {
            if (activeInSolution.stream().anyMatch(v::equals)) {
                v.increaseTimesUsed();
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
        var oldSize = kernel.size();
        kernel.removeIf(v -> v.isFromBucket() && violatesThreshold(solutionsCount, v.getTimesUsed(), threshold));

        return kernel.size() - oldSize;
    }

    public void resetUsages() {
        kernel.forEach(Variable::resetTimesUsed);
    }

    private boolean violatesThreshold(int solutionsCount, int timesUsed, int threshold) {
        var timesNotUsed = solutionsCount - timesUsed;
        return timesNotUsed - timesUsed >= threshold;
    }
}
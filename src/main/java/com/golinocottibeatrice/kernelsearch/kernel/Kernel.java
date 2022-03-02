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
    private List<Variable> kernel = new ArrayList<>();

     /**
     * Aggiunge un {@link Variable} alla lista e inizializza il contatore della eject.
     *
     * @param v {@link Variable} da aggiungere alla lista.
     */
    public void addItem(Variable v, int solutionsCount) {
        v.resetTimesUsed(solutionsCount);
        kernel.add(v);
    }

    /**
     * Aggiunge un {@link Variable} alla lista.
     *
     * @param v {@link Variable} da aggiungere alla lista.
     */
    public void addItem(Variable v) {
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
        List<Variable> activeInSolution =
                solution.getVariables()
                        .stream()
                        .filter(variable -> variable.getValue() >= 1)
                        .toList();

        kernel.forEach(v -> {
            if (activeInSolution.stream().anyMatch(v::equals)) {
                v.increaseTimesUsed();
            }
        });
    }

    /**
     * Esegue eject procedure. Rimuove variabile se #volte non usata - #volte usata >= threshold
     *
     * @param threshold the threshold defined for the eject procedure
     * @param solutions_count numero di soluzioni create durante esecuzione di questa iterazione
     * @return total of variables removed from the kernel
     */
    public int checkForEject(int threshold, int solutions_count) {
        List<Variable> new_variables = this.kernel.stream()
                .filter(variable ->
                        !variable.isFromBucket() || variable.exceedsThreshold(threshold, solutions_count))
                .collect(Collectors.toList());

        int removed_vars = this.kernel.size() - new_variables.size();

        this.kernel = new_variables;

        return removed_vars;
    }
}
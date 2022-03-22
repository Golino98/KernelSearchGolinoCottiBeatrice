package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.solver.Solution;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KernelMap extends Kernel{
    private final Map<String, Variable> kernel = new HashMap<>();

    /**
     * Aggiunge un {@link Variable} alla lista e inizializza il contatore della eject.
     *
     * @param v {@link Variable} da aggiungere alla lista.
     */
    public void addItem(Variable v, int solutionsCount) {
        v.resetTimesUsed(solutionsCount);
        this.addItem(v);
    }

    /**
     * Aggiunge un {@link Variable} alla lista.
     *
     * @param v {@link Variable} da aggiungere alla lista.
     */
    public void addItem(Variable v) {
        kernel.put(v.getName(), v);
    }

    /**
     * Verifica la presenza di un {@link Variable} all'interno della lista.
     *
     * @param v {@link Variable} di cui verificare la presenza all'interno della lista.
     */
    public boolean contains(Variable v) {
        return kernel.containsKey(v.getName());
    }

    /**
     * @return La dimensione del kernel.
     */
    public int size() {
        return kernel.size();
    }

    public List<Variable> getVariables() {
        return kernel.values().stream().toList();
    }

    /**
     * Updates the 'timesUsed' attribute for the variables in the Kernel
     *
     * @param solution the current solution used for the update
     */
    public void updateUsages(Solution solution) {
        solution.getVariables()
                .stream()
                .filter(variable -> variable.getValue() >= 1)
                .forEach(variable -> {
                    kernel.get(variable.getName()).increaseTimesUsed();
                });
        // fixme: get returns null
    }

    /**
     * Esegue eject procedure. Rimuove variabile se #volte non usata - #volte usata >= threshold
     *
     * @param threshold the threshold defined for the eject procedure
     * @param solutions_count numero di soluzioni create durante esecuzione di questa iterazione
     * @return total of variables removed from the kernel
     */
    public int checkForEject(int threshold, int solutions_count) {
        int old_size = this.kernel.size();

        this.kernel.keySet().forEach(key -> {
            Variable variable = this.kernel.get(key);
            if (variable.isFromBucket() && !variable.respectsThreshold(threshold, solutions_count))
                this.kernel.remove(key);
        });

        return old_size - this.kernel.size();
    }
}

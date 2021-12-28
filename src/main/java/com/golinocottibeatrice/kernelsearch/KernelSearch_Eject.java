package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Solution;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public class KernelSearch_Eject extends KernelSearch{

    /**
     * Crea una nuova istanza di kernel search.
     *
     * @param config La configurazione del metodo.
     */
    public KernelSearch_Eject(SearchConfiguration config) {
        super(config);
    }

    /**
     * Checks if an eject procedure is specified and executes it
     *
     * @param selected list of variables selected from the bucket
     * @param solution the current solution
     * @param count_solutions number of solutions visited in the current iteration
     */
    @Override
    protected void executeEject(List<Variable> selected, Solution solution, int count_solutions) {
        this.kernel.updateUsages(solution);
        int removed_vars = this.kernel.checkForEject(this.config.getEjectThreshold(), count_solutions);

        log.solution(selected.size(), kernel.size(), solution.getObjective(), elapsedTime(), removed_vars);
    }

    @Override
    protected void resetUsages() {
        this.kernel.resetUsages();
    }
}
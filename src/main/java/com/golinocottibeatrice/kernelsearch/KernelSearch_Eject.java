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
        List<Variable> removed_vars = this.kernel.checkForEject(this.config.getEjectThreshold(), count_solutions);

        removed_vars.forEach(variable -> {
            if (variable.getBucket() != null) {
                variable.getBucket().addVariable(variable);
            }else {
                this.buckets.get(0).addVariable(variable);
            }
        });

        log.solution(selected.size(), kernel.size(), solution.getObjective(), elapsedTime(), removed_vars.size());
    }

    @Override
    protected void resetUsages() {
        this.kernel.resetUsages();
    }
}

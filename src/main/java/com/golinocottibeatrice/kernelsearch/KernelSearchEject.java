package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Solution;
import com.golinocottibeatrice.kernelsearch.solver.Variable;
import gurobi.GRBException;

import java.util.List;

public class KernelSearchEject extends KernelSearch {
    private int visitedSolutions; // total number of visited solutions

    /**
     * Crea una nuova istanza di kernel search.
     *
     * @param config La configurazione del metodo.
     */
    public KernelSearchEject(SearchConfiguration config) {
        super(config);
    }

    /**
     * Checks if an eject procedure is specified and executes it
     *
     * @param selected        list of variables selected from the bucket
     * @param solution        the current solution
     */
    @Override
    protected void executeEject(List<Variable> selected, Solution solution) {
        this.kernel.updateUsages(solution);
        int removedVars = this.kernel.checkForEject(this.config.getEjectThreshold(), this.visitedSolutions);

        log.solution(selected.size(), removedVars, kernel.size(), solution.getObjective(), timer.elapsedTime());
    }

    @Override
    protected void placeInKernel(List<Variable> selected) {
        this.visitedSolutions++;

        selected.forEach(variable -> {
            if (!this.kernel.contains(variable)) {
                this.kernel.addItem(variable, this.visitedSolutions);
            }
        });
    }

    @Override
    protected void iterateBuckets() throws GRBException {
        this.visitedSolutions = 0;

        for (int i = 0; i < config.getNumIterations(); i++) {
            if (timer.timeLimitReached()) {
                log.timeLimit();
                return;
            }

            log.iterationStart(i);

            solveBuckets();
        }
    }
}

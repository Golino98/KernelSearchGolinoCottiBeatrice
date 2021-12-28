package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Model;
import com.golinocottibeatrice.kernelsearch.solver.Solution;
import com.golinocottibeatrice.kernelsearch.solver.Variable;
import gurobi.GRBException;

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

    protected void iterateBuckets() throws GRBException {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (getRemainingTime() <= TIME_THRESHOLD) {
                log.timeLimit();
                return;
            }

            log.iterationStart(i);

            if (this.config.getEjectThreshold()>0) {
                this.kernel.resetUsages();
            }

            solveBuckets();
        }
    }

    @Override
    protected void solveBuckets() throws GRBException {
        int count = 0;
        int count_solutions = 0;

        for (var b : buckets) {
            count++;

            Model model = this.buildModel(b, count);
            var solution = model.solve();

            if (!solution.isEmpty()) {
                count_solutions++;
                bestSolution = solution;

                // Prendi le variabili del bucket che compaiono nella nuova soluzione trovata,
                // aggiungile al kernel, e rimuovile dal bucket
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(variable -> {variable.setBucket(b); this.kernel.addItem(variable); b.removeItem(variable);});

                this.executeEject(selected, solution, count_solutions);

                model.write();
            } else {
                log.noSolution(elapsedTime());
            }

            model.dispose();

            if (getRemainingTime() <= TIME_THRESHOLD) {
                return;
            }
        }
    }

    /**
     * Checks if an eject procedure is specified and executes it
     *
     * @param selected list of variables selected from the bucket
     * @param solution the current solution
     * @param count_solutions number of solutions visited in the current iteration
     */
    private void executeEject(List<Variable> selected, Solution solution, int count_solutions) {
        if (this.config.getEjectThreshold() > 0) {
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
        } else {
            log.solution(selected.size(), kernel.size(), solution.getObjective(), elapsedTime());
        }
    }

    protected double elapsedTime() {
        var time = (double)(System.nanoTime()-startTime);
        return time/1_000_000_000;
    }

    // Restituisce il tempo rimamente per l'esecuzione
    protected long getRemainingTime() {
        var time = config.getTimeLimit() - (long) elapsedTime();
        return time >= 0 ? time : 0;
    }
}

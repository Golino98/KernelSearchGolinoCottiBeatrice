package com.golinocottibeatrice.kernelsearch.solver;

import gurobi.*;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.IntAttr;
import gurobi.GRB.StringAttr;

import java.util.*;

/**
 * Risolutore per il problema della multiple knapsack,
 * basato su GUROBI.
 */
public class Solver {
    private final SolverConfiguration config;
    private final GRBModel model;

    /**
     * Crea un nuovo risolutore.
     *
     * @param config La configurazione del risolutore.
     * @throws GRBException Errore di GUROBI.
     */
    public Solver(SolverConfiguration config) throws GRBException {
        this.config = config;
        model = new ModelCreator(config).create();
    }

    /**
     * Risolve l'istanza del problema dato.
     *
     * @return La soluzione trovata.
     * @throws GRBException Errore di GUROBI.
     */
    public Solution solve() throws GRBException {
        model.optimize();

        // Nessuna soluzione trovata
        if (model.get(IntAttr.SolCount) == 0) {
            return new EmptySolution();
        }

        var objective = model.get(DoubleAttr.ObjVal);
        var variables = new ArrayList<Variable>();
        for (var v : model.getVars()) {
            Variable variable;
            if (config.isLpRelaxation()) {
                variable = new Variable(v.get(StringAttr.VarName), v.get(DoubleAttr.X), v.get(DoubleAttr.RC));
            } else {
                variable = new Variable(v.get(StringAttr.VarName), v.get(DoubleAttr.X));
            }
            variables.add(variable);
        }
        return new Solution(objective, variables);
    }

    /**
     * Disabilita una variabile, fissando il suo valore a 0.
     *
     * @param v La variabile da disabilitare
     * @throws GRBException Errore di GUROBI.
     */
    public void disableVariable(Variable v) throws GRBException {
        var test = model.getVarByName(v.getName());
        model.addConstr(model.getVarByName(v.getName()), GRB.EQUAL, 0, "FIX_VAR_" + v.getName());
    }

    /**
     * Disabilita delle variabili, fissando il loro valore a 0.
     *
     * @param variables Le variabili da disabilitare.
     * @throws GRBException Errore di GUROBI.
     */
    public void disableVariables(List<Variable> variables) throws GRBException {
        for (var v : variables) {
            disableVariable(v);
        }
    }

    public void addBucketConstraint(List<Variable> variables) throws GRBException {
        var expr = new GRBLinExpr();
        for (var v : variables) {
            expr.addTerm(1, model.getVarByName(v.getName()));
        }
        model.addConstr(expr, GRB.GREATER_EQUAL, 1, "bucketConstraint");
    }

    public void addObjConstraint(double obj) throws GRBException {
        model.getEnv().set(GRB.DoubleParam.Cutoff, obj);
    }

    public void readSolution(Solution solution) throws GRBException {
        for (GRBVar var : model.getVars()) {
            var.set(DoubleAttr.Start, solution.getVariableValue(var.get(StringAttr.VarName)));
        }
    }

    public void setCallback(GRBCallback callback) {
        model.setCallback(callback);
    }

    public List<Variable> getSelectedVariables(List<Variable> variables) throws GRBException {
        var selected = new ArrayList<Variable>();
        for (var v : variables) {
            if (model.getVarByName(v.getName()).get(DoubleAttr.X) > config.getPositiveThreshold()) {
                selected.add(v);
            }
        }
        return selected;
    }

    public void dispose() {
        model.dispose();
    }
}
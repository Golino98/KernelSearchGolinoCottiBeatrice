package com.golinocottibeatrice.kernelsearch.solver;

import gurobi.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper di un modello GUROBI.
 * Rappresenta un'istanza del problema MKP risolvibile tramite un risolutore.
 * <p>
 * Al termine dell'utilizzo il metodo <code>dispose()</code> dovrebbe
 * essere chiamato per liberare le risorse.
 */
public class Model {
    private static final double POSITIVE_THRESHOLD = 1e-5;
    private static final String FORMAT_FIX_VAR = "FIX_VAR_%s";
    private static final String FORMAT_BUCKET = "BUCKET";

    private final GRBModel model;
    private final boolean isLpRelaxation;

    /**
     * Crea un nuovo modello.
     *
     * @param model          Il modello GUROBI.
     * @param isLpRelaxation Indica se il problema deve essere rilassato.
     */
    Model(GRBModel model, boolean isLpRelaxation) throws GRBException {
        this.model = isLpRelaxation ? model.relax() : model;
        this.isLpRelaxation = isLpRelaxation;
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
        if (model.get(GRB.IntAttr.SolCount) == 0) {
            return new EmptySolution();
        }

        var objective = model.get(GRB.DoubleAttr.ObjVal);
        var variables = new ArrayList<Variable>();
        for (var v : model.getVars()) {
            var rc = isLpRelaxation ? v.get(GRB.DoubleAttr.RC) : 0;
            var variable = new Variable(v.get(GRB.StringAttr.VarName), v.get(GRB.DoubleAttr.X), rc);
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
        var name = String.format(FORMAT_FIX_VAR, v.getName());
        model.addConstr(model.getVarByName(v.getName()), GRB.EQUAL, 0, name);
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
        model.addConstr(expr, GRB.GREATER_EQUAL, 1, FORMAT_BUCKET);
    }

    public void addObjConstraint(double obj) throws GRBException {
        model.getEnv().set(GRB.DoubleParam.Cutoff, obj);
    }

    public void readSolution(Solution solution) throws GRBException {
        for (GRBVar var : model.getVars()) {
            var.set(GRB.DoubleAttr.Start, solution.getVariableValue(var.get(GRB.StringAttr.VarName)));
        }
    }

    public List<Variable> getSelectedVariables(List<Variable> variables) throws GRBException {
        var selected = new ArrayList<Variable>();
        for (var v : variables) {
            if (model.getVarByName(v.getName()).get(GRB.DoubleAttr.X) > POSITIVE_THRESHOLD) {
                selected.add(v);
            }
        }
        return selected;
    }

    public void write(String path) throws GRBException {
        model.write(path);
    }

    public void dispose() {
        model.dispose();
    }
}

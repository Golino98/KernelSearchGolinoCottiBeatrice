package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.additions.DominanceList;
import gurobi.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper di un modello GUROBI.
 * Rappresenta un'istanza del problema MKP risolvibile tramite un risolutore.
 * <p>
 * Al termine dell'utilizzo il metodo {@code dispose()} dovrebbe
 * essere chiamato per liberare le risorse.
 */
public class Model {
    private static final double POSITIVE_THRESHOLD = 1.0e-5;
    private static final String FORMAT_FIX_VAR = "FIX_VAR_%s";
    private static final String FORMAT_BUCKET = "BUCKET";

    private final GRBModel model;
    private final ModelConfiguration config;

    /**
     * Crea un nuovo modello.
     *
     * @param model  Il modello GUROBI.
     * @param config La configurazione del modello.
     */
    Model(GRBModel model, ModelConfiguration config) throws GRBException {
        this.model = config.isLpRelaxation() ? model.relax() : model;
        this.config = config;

        model.set(GRB.DoubleParam.TimeLimit, config.getTimeLimit());
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
        for (var v : config.getVariables()) {
            var grbVar = model.getVarByName(v.getName());
            var rc = config.isLpRelaxation() ? grbVar.get(GRB.DoubleAttr.RC) : 0;
            var value = grbVar.get(GRB.DoubleAttr.X);
            v.setRc(rc);
            v.setValue(value);
        }
        return new Solution(objective, config.getVariables());
    }

    /**
     * Disabilita una variabile, fissando il suo valore a 0.
     *
     * @param v La variabile da disabilitare
     * @throws GRBException Errore di GUROBI.
     */
    public void disableVariable(Variable v) throws GRBException {
        var name = String.format(FORMAT_FIX_VAR, v.getName());
        var n = model.getVarByName(v.getName());
        if (n == null) {
            var s = model.getVars();
            System.out.println("a");
        }
        model.addConstr(n, GRB.EQUAL, 0, name);
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

    /**
     * Aggiunge un vincolo che impone che tra tutte le variabili di un bucket
     * ne deve essere abilitata almeno una
     *
     * @param variables Le variabili del bucket.
     * @throws GRBException Errore di GUROBI.
     */
    public void addBucketConstraint(List<Variable> variables) throws GRBException {
        var expr = new GRBLinExpr();
        for (var v : variables) {
            expr.addTerm(1, model.getVarByName(v.getName()));
        }
        model.addConstr(expr, GRB.GREATER_EQUAL, 1, FORMAT_BUCKET);
    }

    /**
     * Aggiunge un vincolo di cutoff al modello.
     *
     * @param obj Il valore di cut.
     * @throws GRBException Errore di GUROBI
     */
    public void addObjConstraint(double obj) throws GRBException {
        model.getEnv().set(GRB.DoubleParam.Cutoff, obj);
    }

    public void addDominanceConstraints(DominanceList dominanceList) throws GRBException {
        for (var pair : dominanceList) {
            var lhs = new GRBLinExpr();
            var rhs = new GRBLinExpr();
            var j = pair.getJ();
            var k = pair.getK();

            for (var i = 0; i < config.getInstance().getNumKnapsacks(); i++) {
                lhs.addTerm(1, config.getGrbVars()[i][j.getIndex()]);
                rhs.addTerm(1, config.getGrbVars()[i][k.getIndex()]);
            }
            var name = String.format("ITEM_DOMINANCE_%d_%d", j.getIndex(), k.getIndex());
            model.addConstr(lhs, GRB.GREATER_EQUAL, rhs, name);
        }
    }

    /**
     * Inizializza le variabili del modello.
     *
     * @param solution La soluzione da cui prendere i valori iniziali.
     * @throws GRBException Errore di GUROBI.
     */
    public void readSolution(Solution solution) throws GRBException {
        for (GRBVar variable : model.getVars()) {
            variable.set(GRB.DoubleAttr.Start, solution.getVariableValue(variable.get(GRB.StringAttr.VarName)));
        }
    }

    /**
     * Restituisce, tra una lista di variabili, solo quelle selezionate nella soluzione.
     *
     * @param variables Le variabili da cui estrarre le variabili selezionate.
     * @return Le variabili selezionate
     * @throws GRBException Errore di GUROBI.
     */
    public List<Variable> getSelectedVariables(List<Variable> variables) throws GRBException {
        var selected = new ArrayList<Variable>();
        for (var v : variables) {
            if (model.getVarByName(v.getName()).get(GRB.DoubleAttr.X) > POSITIVE_THRESHOLD) {
                selected.add(v);
            }
        }
        return selected;
    }

    /**
     * Scrive la soluzione del modello sul file di soluzione.
     *
     * @throws GRBException Errore di GUROBI.
     */
    public void write() throws GRBException {
        model.write(config.getSolPath());
    }

    /**
     * Libera le risorse occupate dal modello.
     */
    public void dispose() {
        model.dispose();
    }
}

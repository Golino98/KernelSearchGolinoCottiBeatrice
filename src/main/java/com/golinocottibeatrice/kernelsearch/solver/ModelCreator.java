package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import gurobi.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Creatore di un nuovo modello, impostato per risolvere il problema MKP.
 */
class ModelCreator {
    private static final String FORMAT_VAR_NAME = "x_%d_%d";
    private static final String FORMAT_CAPACITY = "MAX_CAPACITY_KNAPSACK_%d";
    private static final String FORMAT_SELECTION = "SELECTION_ITEM_%d";

    private final ModelConfiguration config;
    private final Instance instance;

    // Modello GUROBI
    private GRBModel model;
    // Variabili binarie x(i,j) che indicano se l'oggetto i viene collocato nel knapsack j
    private GRBVar[][] grbVars;
    // Variabli che incapsulano l'informazione delle variabili di GUROBI e del relativo item
    private final List<Variable> modelVars = new ArrayList<>();

    ModelCreator(ModelConfiguration config) {
        this.config = config;
        instance = config.getInstance();
    }

    /**
     * Crea il modello GUROBI dell'istanza del problema.
     *
     * @return Il modello GUROBI.
     * @throws GRBException Errore nella creazione del modello.
     */
    public Model create() throws GRBException {
        model = new GRBModel(config.getEnv());

        initializeVariables();
        createCapacityContraints();
        createSelectionContraints();
        setObjectiveFunction();

        config.setVariables(modelVars);
        config.setGrbVars(grbVars);

        // Necessario per applicare le modifiche effettuate al modello.
        model.update();
        return new Model(model, config);
    }

    // Crea le di variabili binarie x(i,j)
    private void initializeVariables() throws GRBException {
        var nk = instance.getNumKnapsacks();
        var ni = instance.getNumItems();
        grbVars = new GRBVar[nk][ni];

        for (var knapsack : instance.getKnapsacks()) {
            for (var item : instance.getItems()) {
                var name = String.format(FORMAT_VAR_NAME, knapsack.getIndex() + 1, item.getIndex() + 1);
                grbVars[knapsack.getIndex()][item.getIndex()] = model.addVar(0, 1, 0, GRB.BINARY, name);
                modelVars.add(new Variable(name, item, knapsack));
            }
        }
    }

    // Sommatoria per j=1..n di w(j)*x(i,j)<=c(i) per i=1..m
    private void createCapacityContraints() throws GRBException {
        for (var knapsack : instance.getKnapsacks()) {
            var constraint = new GRBLinExpr();
            for (var item : instance.getItems()) {
                constraint.addTerm(item.getWeight(), grbVars[knapsack.getIndex()][item.getIndex()]);
            }
            var name = String.format(FORMAT_CAPACITY, knapsack.getIndex() + 1);
            model.addConstr(constraint, GRB.LESS_EQUAL, knapsack.getCapacity(), name);
        }
    }

    // Sommatoria per i=1..m di x(i,j) <=1 per j=1..n
    private void createSelectionContraints() throws GRBException {
        for (var item : instance.getItems()) {
            var constraint = new GRBLinExpr();
            for (var knapsack : instance.getKnapsacks()) {
                constraint.addTerm(1, grbVars[knapsack.getIndex()][item.getIndex()]);
            }
            var name = String.format(FORMAT_SELECTION, item.getIndex() + 1);
            model.addConstr(constraint, GRB.LESS_EQUAL, 1, name);
        }
    }

    // Max della sommatoria per i=1..m di sommatoria per j=1..n di p(j)*x(i,j)
    private void setObjectiveFunction() throws GRBException {
        var objective = new GRBLinExpr();
        for (var knapsack : instance.getKnapsacks()) {
            for (var item : instance.getItems()) {
                objective.addTerm(item.getProfit(), grbVars[knapsack.getIndex()][item.getIndex()]);
            }
        }
        model.setObjective(objective, GRB.MAXIMIZE);
    }
}

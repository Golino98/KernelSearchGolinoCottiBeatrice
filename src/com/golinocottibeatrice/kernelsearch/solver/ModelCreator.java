package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import gurobi.*;

/**
 * Creatore di un nuovo modello, impostato per risolvere il problema MKP.
 */
class ModelCreator {
    public static final String FORMAT_VAR_NAME = "x_%d_%d";
    private static final String FORMAT_CAPACITY = "Vincolo sulla capacità per zaino %d";
    private static final String FORMAT_SELECTION = "Vincolo massima selezione per item %d";

    private final ModelConfiguration config;
    private final Instance instance;

    // Modello GUROBI
    private GRBModel model;
    // Variabili binarie x(i,j) che indicano se l'oggetto i viene collocato nel knapsack j
    private GRBVar[][] variables;

    ModelCreator(ModelConfiguration config) {
        this.config = config;
        this.instance = config.getInstance();
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

        // Necessario per applicare le modifiche effettuate al modello.
        model.update();
        return new Model(model, config);
    }

    // Crea le di variabili binarie x(i,j)
    private void initializeVariables() throws GRBException {
        var nk = instance.getNumKnapsacks();
        var ni = instance.getNumItems();
        variables = new GRBVar[nk][ni];

        for (var knapsack = 0; knapsack < nk; knapsack++) {
            for (int item = 0; item < ni; item++) {
                var name = String.format(FORMAT_VAR_NAME, knapsack + 1, item + 1);
                variables[knapsack][item] = model.addVar(0, 1, 0, GRB.BINARY, name);
            }
        }
    }

    // Sommatoria per j=1..n di w(j)*x(i,j)<=c(i) per i=1..m
    private void createCapacityContraints() throws GRBException {
        for (int knapsack = 0; knapsack < instance.getNumKnapsacks(); knapsack++) {
            var constraint = new GRBLinExpr();
            for (int item = 0; item < instance.getNumItems(); item++) {
                constraint.addTerm(instance.getWeight(item), variables[knapsack][item]);
            }
            var name = String.format(FORMAT_CAPACITY, knapsack + 1);
            model.addConstr(constraint, GRB.LESS_EQUAL, instance.getCapacity(knapsack), name);
        }
    }

    // Sommatoria per i=1..m di x(i,j) <=1 per j=1..n
    private void createSelectionContraints() throws GRBException {
        for (int item = 0; item < instance.getNumItems(); item++) {
            var constraint = new GRBLinExpr();
            for (int knapsack = 0; knapsack < instance.getNumKnapsacks(); knapsack++) {
                constraint.addTerm(1, variables[knapsack][item]);
            }
            var name = String.format(FORMAT_SELECTION, item + 1);
            model.addConstr(constraint, GRB.LESS_EQUAL, 1, name);
        }
    }

    // Max della sommatoria per i=1..m di sommatoria per j=1..n di p(j)*x(i,j)
    private void setObjectiveFunction() throws GRBException {
        var objective = new GRBLinExpr();
        for (int knapsack = 0; knapsack < instance.getNumKnapsacks(); knapsack++) {
            for (int item = 0; item < instance.getNumItems(); item++) {
                objective.addTerm(instance.getProfit(item), variables[knapsack][item]);
            }
        }
        model.setObjective(objective, GRB.MAXIMIZE);
    }
}

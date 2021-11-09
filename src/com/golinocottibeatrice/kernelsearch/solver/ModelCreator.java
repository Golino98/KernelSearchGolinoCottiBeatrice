package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import gurobi.*;

/**
 * Creatore del modello GUROBI con vincoli e funzione obiettivo,
 * sulla base dell'istanza del problema multiple knapsack.
 */
class ModelCreator {
    private static final String FORMATO_NOME_VARIABILE = "x_%d_%d";
    private static final String FORMATO_VINCOLO_CAPACITA = "Vincolo sulla capacità per zaino %d";
    private static final String FORMATO_VINCOLO_SELEZIONE = "Vincolo massima selezione per item %d";

    private final SolverConfiguration config;
    private final Instance instance;

    // Modello GUROBI
    private GRBModel model;
    // Variabili binarie x(i,j) che indicano se l'oggetto i viene collocato nel knapsack j
    private GRBVar[][] variables;

    public ModelCreator(SolverConfiguration config) {
        this.config = config;
        this.instance = config.getInstance();
    }

    /**
     * Crea il modello GUROBI dell'istanza del problema.
     *
     * @return Il modello GUROBI.
     * @throws GRBException Errore nella creazione del modello.
     */
    public GRBModel create() throws GRBException {
        var env = new GRBEnv();
        env.set(GRB.StringParam.LogFile, config.getLogPath());
        env.set(GRB.StringParam.ResultFile, "./log/best.sol");
        env.set(GRB.IntParam.Threads, config.getNumThreads());
        env.set(GRB.IntParam.Presolve, config.getPresolve());
        env.set(GRB.DoubleParam.MIPGap, config.getMipGap());
        if (config.getTimeLimit() > 0) {
            env.set(GRB.DoubleParam.TimeLimit, config.getTimeLimit());
        }

        model = new GRBModel(env);
        buildModel();
        return model;
    }

    private void buildModel() throws GRBException {
        initializeVariables();
        createCapacityContraints();
        createSelectionContraints();
        setObjectiveFunction();

        model.update();
        if (config.isLpRelaxation()) {
            model = model.relax();
        }
    }

    // Crea le di variabili binarie x(i,j)
    private void initializeVariables() throws GRBException {
        var nk = instance.getNumKnapsacks();
        var ni = instance.getNumItems();
        variables = new GRBVar[nk][ni];

        for (var knapsack = 0; knapsack < nk; knapsack++) {
            for (int item = 0; item < ni; item++) {
                var name = String.format(FORMATO_NOME_VARIABILE, knapsack + 1, item + 1);
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
            var name = String.format(FORMATO_VINCOLO_CAPACITA, knapsack + 1);
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
            var name = String.format(FORMATO_VINCOLO_SELEZIONE, item + 1);
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

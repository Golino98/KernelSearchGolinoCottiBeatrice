package com.golino.cotti.classes;

import com.golino.cotti.classes.instance.Instance;
import gurobi.*;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.IntAttr;
import gurobi.GRB.StringAttr;

import static com.golino.cotti.classes.Costanti.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Model {
    private final ModelConfiguration config;
    private Instance instance;
    private GRBModel model;
    private GRBLinExpr linExpr;
    private boolean hasSolution;

    private GRBVar[][] x;

    public Model(ModelConfiguration config) throws GRBException {
        this.config = config;
        this.instance = config.getInstance();
        this.hasSolution = false;

        var env = new GRBEnv();
        env.set(GRB.StringParam.LogFile, config.getLogPath());
        env.set(GRB.IntParam.Threads, config.getNumThreads());
        env.set(GRB.IntParam.Presolve, config.getPresolve());
        env.set(GRB.DoubleParam.MIPGap, config.getMipGap());
        if (config.getTimeLimit() > 0) {
            env.set(GRB.DoubleParam.TimeLimit, config.getTimeLimit());
        }

        model = new GRBModel(env);
        readAndSetData();
        if (config.isLpRelaxation()) {
            model = model.relax();
        }
    }

    private void readAndSetData() throws GRBException {
        createCapacityContraints();
        createSelectionContraint();

        setObjectiveFunction();

        model.setObjective(linExpr, GRB.MAXIMIZE);
        model.optimize();
    }

    public void solve() throws GRBException {
        model.optimize();
        if (model.get(IntAttr.SolCount) > 0) {
            hasSolution = true;
        }
    }

    public List<String> getVarNames() throws GRBException {
        List<String> varNames = new ArrayList<>();
        for (GRBVar v : model.getVars()) {
            varNames.add(v.get(StringAttr.VarName));
        }
        return varNames;
    }

    public double getVarValue(String v) throws GRBException {
        if (model.get(IntAttr.SolCount) > 0) {
            return model.getVarByName(v).get(DoubleAttr.X);
        }
        return -1;
    }

    public double getVarRC(String v) throws GRBException {
        if (model.get(IntAttr.SolCount) > 0) {
            return model.getVarByName(v).get(DoubleAttr.RC);
        }
        return -1;
    }

    public void disableItems(List<Item> items) throws GRBException {
        for (Item it : items) {
            model.addConstr(model.getVarByName(it.getName()), GRB.EQUAL, 0, "FIX_VAR_" + it.getName());
        }
    }

    public void exportSolution() throws GRBException {
        model.write("bestSolution.sol");
    }

    public void readSolution(Solution solution) throws GRBException {
        for (GRBVar var : model.getVars()) {
            var.set(DoubleAttr.Start, solution.getVarValue(var.get(StringAttr.VarName)));
        }
    }

    public boolean hasSolution() {
        return hasSolution;
    }

    public Solution getSolution() throws GRBException {
        Solution sol = new Solution();

        sol.setObj(model.get(DoubleAttr.ObjVal));
        Map<String, Double> vars = new HashMap<>();
        for (GRBVar var : model.getVars()) {
            vars.put(var.get(StringAttr.VarName), var.get(DoubleAttr.X));
        }
        sol.setVars(vars);
        return sol;
    }

    public void addBucketConstraint(List<Item> items) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        for (Item it : items) {
            expr.addTerm(1, model.getVarByName(it.getName()));
        }
        model.addConstr(expr, GRB.GREATER_EQUAL, 1, "bucketConstraint");
    }

    public void addObjConstraint(double obj) throws GRBException {
        model.getEnv().set(GRB.DoubleParam.Cutoff, obj);
    }

    public List<Item> getSelectedItems(List<Item> items) throws GRBException {
        List<Item> selected = new ArrayList<>();
        for (Item it : items) {
            if (model.getVarByName(it.getName()).get(DoubleAttr.X) > config.getPositiveThreshold()) {
                selected.add(it);
            }
        }
        return selected;
    }

    public void setCallback(GRBCallback callback) {
        model.setCallback(callback);
    }

    public void setVariables(int number_of_knapsacks, int number_of_items) throws GRBException {
        x = new GRBVar[number_of_knapsacks][number_of_items];
        for (int i = 0; i < number_of_knapsacks; i++) {
            for (int j = 0; j < number_of_items; j++) {
                x[i][j] = model.addVar(0, 1, 0, GRB.BINARY, "x_" + (i + 1) + "_" + (j + 1));
            }
        }
    }

    private void createCapacityContraints() throws GRBException {
        for (int i = 0; i < instance.getNumKnapsacks(); i++) {
            linExpr = new GRBLinExpr();
            for (int j = 0; j < instance.getNumItems(); j++) {
                linExpr.addTerm(instance.getItems().get(j).getWeight(), x[i][j]);
            }
            model.addConstr(linExpr, GRB.LESS_EQUAL, instance.getCapacities().get(i), VINCOLO_CAPACITA + (i + 1));
        }
    }

    private void createSelectionContraint() throws GRBException {
        for (int j = 0; j < instance.getNumItems(); j++) {
            linExpr = new GRBLinExpr();
            for (int i = 0; i < instance.getNumKnapsacks(); i++) {
                linExpr.addTerm(1, x[i][j]);
            }
            model.addConstr(linExpr, GRB.LESS_EQUAL, 1, VINCOLO_SELEZIONE_ZAINO + (j + 1));
        }
    }

    private void setObjectiveFunction() throws GRBException {
        linExpr = new GRBLinExpr();
        for (int i = 0; i < instance.getNumKnapsacks(); i++) {
            for (int j = 0; j < instance.getNumItems(); j++) {
                linExpr.addTerm(instance.getItems().get(j).getProfit(), x[i][j]);
            }
        }
    }
}
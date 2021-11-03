package com.golino.cotti.classes;

import gurobi.*;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.IntAttr;
import gurobi.GRB.StringAttr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private final ModelConfiguration config;
    private GRBModel model;
    private boolean hasSolution;

    public Model(ModelConfiguration config) throws GRBException {
        this.config = config;
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
        if (config.isLpRelaxation()) {
            model = model.relax();
        }
    }

    private void addVariables() {
        /*
       try (BufferedReader br = Files.newBufferedReader(Paths.get("C:\\Users\\giaco\\IdeaProjects\\TestFunzioniCodice\\src\\com\\giacomo\\golino\\test.txt"))) {

            List<String> lines;
            lines = br.lines().collect(Collectors.toList());

            int number_of_knapsacks = Integer.parseInt(lines.get(0));

            //int capacita[] = new int[number_of_knapsacks];
            ArrayList<Integer> cap = new ArrayList<>();

            int number_of_items = Integer.parseInt(lines.get(1));

            for (String line : lines.subList(2, number_of_knapsacks + 2)) {
                //capacita[k] = Integer.parseInt(line);
                cap.add(Integer.parseInt(line));
            }

            ArrayList<Item> items = new ArrayList<>();
            //Item items[] = new Item[number_of_items];

            for (String line : lines.subList(number_of_knapsacks + 2, number_of_items + number_of_knapsacks + 2)) {

                String[] splitLine = line.split("\\s+");
                items.add(new Item(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1])));
            }

            GRBEnv env = new GRBEnv("test.log");

            env.set(GRB.IntParam.Presolve, 2);
            env.set(GRB.IntParam.Method, 0);
            env.set(GRB.IntParam.Threads, 0);

            GRBModel model = new GRBModel(env);

            GRBVar[][] x = new GRBVar[number_of_knapsacks][number_of_items];
            for (int i = 0; i < number_of_knapsacks; i++) {
                for (int j = 0; j < number_of_items; j++) {
                    x[i][j] = model.addVar(0, 1, 0, GRB.BINARY, "x_" + i + "_" + j);
                }
            }

            GRBLinExpr linExpr;

            //Sommatoria peso * variabile <= capacità dello zaino
            int nu = 1;
            for (int i = 0; i < number_of_knapsacks; i++) {
                linExpr = new GRBLinExpr();
                for (int j = 0; j < number_of_items; j++) {
                    //linExpr.addTerm(items[j].getPeso(), x[i][j]);
                    linExpr.addTerm(items.get(j).getPeso(), x[i][j]);
                }
                model.addConstr(linExpr, GRB.LESS_EQUAL, cap.get(i), "c2_" + i + "_" + nu);
                nu++;
            }

            //Sommatoria da i = 1 fino a m di xij <= 1
            nu = 1;
            for (int j = 0; j < number_of_items; j++) {
                linExpr = new GRBLinExpr();
                for (int i = 0; i < number_of_knapsacks; i++) {
                    linExpr.addTerm(1, x[i][j]);
                }
                model.addConstr(linExpr, GRB.LESS_EQUAL, 1, "c_" + nu++ + "_" + j);
            }

            linExpr = new GRBLinExpr();
            for(int i = 0; i < number_of_knapsacks; i++)
            {
                for(int j = 0; j < number_of_items; j++)
                {
                    linExpr.addTerm(items.get(j).getProfitto(), x[i][j]);
                }
            }

            model.setObjective(linExpr,GRB.MAXIMIZE);
            model.optimize();
        */
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
}
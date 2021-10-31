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
        try (BufferedReader br = Files.newBufferedReader(Paths.get(config.getInstPath()))) {
            var lines = br.lines().collect(Collectors.toList());

            int number_of_knapsacks = Integer.parseInt(lines.get(0));
            int number_of_items = Integer.parseInt(lines.get(1));

             * Il seguente ciclo va a suddividere la {@link List<String>} in una sottostringa, prendendo come primo riferimento
             * l'elemento in posizione due della lista (capacità del primo knapsack) e come ultimo riferimento l'ultima capacità
             * dell'ultimo knapsack, presente in posizione 2 + (numero dei knapsack presenti).
             * Si va a sommare il valore due in quanto le prime due righe del file contenente i dati contengono il numero dei
             * knapsacks e degli items.
             * Inoltre viene effettuato un controllo di correttezza dei dati, ovvero, devono essere stati inseriti tutti e devono
             * essere tutti maggiori di zero
            for (String line : lines.subList(2, 2 + number_of_knapsacks)) {
                if (line.isBlank()) {
                    throw new IOException(ERRORE_KNAPSACK_CAPACITA);
                } else if (Integer.parseInt(line) <= 0) {
                    throw new InvalidAttributeValueException(ERRORE_KNAPSACK_VALORE);
                }
            }

             * Il seguente ciclo va a suddividere la {@link List<String>} in una sottostringa, prendendo come primo riferimento
             * il numero dei knapsacks presenti e sommandogli due (in quanto le prime due righe sono utilizzate per la definizione
             * del numero di knapsacks e di items) e come ultimo riferimento l'elemento in posizione finale, ovvero quella
             * corrispondente al numero di item sommata al numero dei knapsack e a due (prime due righe per la definizione del numero
             * di knapsacks e items).
             * Inoltre viene effettuato un controllo di correttezza dei dati, ovvero, devono essere stati inseriti tutti e devono
             * essere tutti maggiori di zero
            for (String line : lines.subList(number_of_knapsacks + 2, number_of_items + number_of_knapsacks + 2)) {
                String[] splitLine = line.split("\\s+");
                if (splitLine[0].isBlank()) {
                    throw new IOException(ERRORE_ITEM_PESO);
                } else if (Integer.parseInt(splitLine[0]) <= 0) {
                    throw new InvalidAttributeValueException(ERRORE_ITEM_PESO_VALORE);
                } else if (splitLine[1].isBlank()) {
                    throw new IOException(ERRORE_ITEM_PROFITTO);
                } else if (Integer.parseInt(splitLine[1]) <= 0) {
                    throw new InvalidAttributeValueException(ERRORE_ITEM_PROFITTO_VALORE);
                }
            }

        } catch (IOException | InvalidAttributeValueException e) {
            e.printStackTrace();
        }
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
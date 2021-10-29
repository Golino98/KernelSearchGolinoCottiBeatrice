package com.golino.cotti.classes;

import gurobi.*;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.IntAttr;
import gurobi.GRB.StringAttr;

import javax.management.InvalidAttributeValueException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.golino.cotti.classes.Costanti.*;

public class Model {
    private final String mpsFilePath;
    private final String logPath;
    private final int timeLimit;
    private final Configuration config;
    private final boolean lpRelaxation;
    private GRBEnv env;
    private GRBModel model;
    private boolean hasSolution;
    private final double positiveThreshold = 1e-5;

    public Model(String mpsFilePath, String logPath, int timeLimit, Configuration config, boolean lpRelaxation) {
        this.mpsFilePath = mpsFilePath;
        this.logPath = logPath;
        this.timeLimit = timeLimit;
        this.config = config;
        this.lpRelaxation = lpRelaxation;
        this.hasSolution = false;
    }

    public void buildModel() {
        try {
            env = new GRBEnv();
            setParameters();
            //model = new GRBModel(env, mpsFilePath);
            model = new GRBModel(env);
            if (lpRelaxation)
                model = model.relax();
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Da controllare con Cotti
     */
    private void addVariables() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(mpsFilePath))) {
            List<String> lines = new ArrayList<>();
            lines = br.lines().collect(Collectors.toList());

            int number_of_knapsacks = Integer.parseInt(lines.get(0));
            int number_of_items = Integer.parseInt(lines.get(1));

            /**
             * Il seguente ciclo va a suddividere la {@link List<String>} in una sottostringa, prendendo come primo riferimento
             * l'elemento in posizione due della lista (capacità del primo knapsack) e come ultimo riferimento l'ultima capacità
             * dell'ultimo knapsack, presente in posizione 2 + (numero dei knapsack presenti).
             * Si va a sommare il valore due in quanto le prime due righe del file contenente i dati contengono il numero dei
             * knapsacks e degli items.
             * Inoltre viene effettuato un controllo di correttezza dei dati, ovvero, devono essere stati inseriti tutti e devono
             * essere tutti maggiori di zero
             */

            for (String line : lines.subList(2, 2+number_of_knapsacks))
            {
                if(line.isBlank())
                {
                    throw new IOException(ERRORE_KNAPSACK_CAPACITA);
                }else if(Integer.parseInt(line)<=0)
                {
                    throw new InvalidAttributeValueException(ERRORE_KNAPSACK_VALORE);
                }
            }

            /**
             * Il seguente ciclo va a suddividere la {@link List<String>} in una sottostringa, prendendo come primo riferimento
             * il numero dei knapsacks presenti e sommandogli due (in quanto le prime due righe sono utilizzate per la definizione
             * del numero di knapsacks e di items) e come ultimo riferimento l'elemento in posizione finale, ovvero quella
             * corrispondente al numero di item sommata al numero dei knapsack e a due (prime due righe per la definizione del numero
             * di knapsacks e items).
             * Inoltre viene effettuato un controllo di correttezza dei dati, ovvero, devono essere stati inseriti tutti e devono
             * essere tutti maggiori di zero
             */
            for (String line : lines.subList(number_of_knapsacks + 2, number_of_items + number_of_knapsacks + 2 ))
            {
                String[] splitLine = line.split("\\s+");
                if(splitLine[0].isBlank())
                {
                    throw new IOException(ERRORE_ITEM_PESO);
                }else if(Integer.parseInt(splitLine[0])<=0)
                {
                    throw new InvalidAttributeValueException(ERRORE_ITEM_PESO_VALORE);
                }else if(splitLine[1].isBlank())
                {
                    throw new IOException(ERRORE_ITEM_PROFITTO);
                }else if (Integer.parseInt(splitLine[1])<=0)
                {
                    throw new InvalidAttributeValueException(ERRORE_ITEM_PESO_VALORE);
                }
            }

        } catch (IOException | InvalidAttributeValueException e) {
            e.printStackTrace();
        }

    }

    private void setParameters() throws GRBException {
        env.set(GRB.StringParam.LogFile, logPath + "log.txt");
        env.set(GRB.IntParam.Threads, config.getNumThreads());
        env.set(GRB.IntParam.Presolve, config.getPresolve());
        env.set(GRB.DoubleParam.MIPGap, config.getMipGap());
        if (timeLimit > 0) {
            env.set(GRB.DoubleParam.TimeLimit, timeLimit);
        }
        //env.set(GRB.IntParam.Method, 0);
        //env.set(IntParam.OutputFlag, 0);

    }

    public void solve() {
        try {
            model.optimize();
            if (model.get(IntAttr.SolCount) > 0)
                hasSolution = true;
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    public List<String> getVarNames() {
        List<String> varNames = new ArrayList<>();

        for (GRBVar v : model.getVars()) {
            try {
                varNames.add(v.get(StringAttr.VarName));
            } catch (GRBException e) {
                e.printStackTrace();
            }
        }
        return varNames;
    }

    public double getVarValue(String v) {
        try {
            if (model.get(IntAttr.SolCount) > 0) {
                return model.getVarByName(v).get(DoubleAttr.X);
            }
        } catch (GRBException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getVarRC(String v) {
        try {
            if (model.get(IntAttr.SolCount) > 0) {
                return model.getVarByName(v).get(DoubleAttr.RC);
            }
        } catch (GRBException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void disableItems(List<Item> items) {
        try {
            for (Item it : items) {
                model.addConstr(model.getVarByName(it.getName()), GRB.EQUAL, 0, "FIX_VAR_" + it.getName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportSolution() {
        try {
            model.write("bestSolution.sol");
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    public void readSolution(String path) {
        try {
            model.read(path);
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    public void readSolution(Solution solution) {
        try {
            for (GRBVar var : model.getVars()) {
                var.set(DoubleAttr.Start, solution.getVarValue(var.get(StringAttr.VarName)));
            }
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    public boolean hasSolution() {
        return hasSolution;
    }

    public Solution getSolution() {
        Solution sol = new Solution();

        try {
            sol.setObj(model.get(DoubleAttr.ObjVal));
            Map<String, Double> vars = new HashMap<>();
            for (GRBVar var : model.getVars()) {
                vars.put(var.get(StringAttr.VarName), var.get(DoubleAttr.X));
            }
            sol.setVars(vars);
        } catch (GRBException e) {
            e.printStackTrace();
        }
        return sol;
    }

    public void addBucketConstraint(List<Item> items) {
        GRBLinExpr expr = new GRBLinExpr();

        try {
            for (Item it : items) {
                expr.addTerm(1, model.getVarByName(it.getName()));
            }
            model.addConstr(expr, GRB.GREATER_EQUAL, 1, "bucketConstraint");
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    public void addObjConstraint(double obj) {
        try {
            model.getEnv().set(GRB.DoubleParam.Cutoff, obj);
        } catch (GRBException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getSelectedItems(List<Item> items) {
        List<Item> selected = new ArrayList<>();
        for (Item it : items) {
            try {
                if (model.getVarByName(it.getName()).get(DoubleAttr.X) > positiveThreshold)
                    selected.add(it);
            } catch (GRBException e) {
                e.printStackTrace();
            }
        }
        return selected;
    }

    public void setCallback(GRBCallback callback) {
        model.setCallback(callback);
    }
}
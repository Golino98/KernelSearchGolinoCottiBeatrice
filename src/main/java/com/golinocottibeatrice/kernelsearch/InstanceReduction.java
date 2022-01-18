package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.instance.Knapsack;
import com.golinocottibeatrice.kernelsearch.solver.Variable;
import gurobi.*;

import java.util.*;

public class InstanceReduction {
    private final List<Knapsack> knapsacks;
    private final List<Item> items;
    private final ArrayList<Knapsack> I;
    private final ArrayList<Item> J;

    public InstanceReduction(Instance instance) {
        this.knapsacks = instance.getKnapsacks().stream().sorted(Comparator.comparing(Knapsack::getCapacity)).toList();
        this.items = instance.getItems();
        this.I = new ArrayList<>();
        this.J = new ArrayList<>();
    }

    public Map<Knapsack, Item> reduce() {
        var packing = new HashMap<Knapsack, Item>();

        for (var i : knapsacks) {
            I.add(i);
            for (var j : items) {
                if (j.getWeight() <= Collections.max(I)) {
                    J.add(j);
                }
            }

            var w = J.stream().mapToInt(Item::getWeight).sum();
            var c = I.stream().mapToInt(Knapsack::getCapacity).sum();
            if (w > c) {
                continue;
            }

            checkPacking();
        }

        return packing;
    }

    public boolean checkPacking() throws GRBException {
        var env = new GRBEnv();
        var model = new GRBModel(env);
        var n = items.size();
        var ub = knapsacks.size();
        var x = new GRBVar[n][ub];
        var y = new GRBVar[ub];

        for (var i = 0; i < n; i++) {
            for (int j = 0; j < ub; j++) {
                x[i][j] = model.addVar(0, 1, 0, GRB.BINARY, "X_" + i + "_" + j);
            }
        }

        for (int j = 0; j < ub; j++) {
            y[j] = model.addVar(0, 1, 0, GRB.BINARY, "Y_" + j);
        }

        var obj = new GRBLinExpr();
        for (int j = 0; j < ub; j++) {
            obj.addTerm(1, y[j]);
        }
        model.setObjective(obj);


        for (var i = 0; i < n; i++) {
            var constraint = new GRBLinExpr();
            for (int j = 0; j < ub; j++) {
                constraint.addTerm(1, x[i][j]);
            }
            model.addConstr(constraint, GRB.EQUAL, 1, "ASS_" + i);
        }

        for (var j = 0; j < ub; j++) {
            var constraint = new GRBLinExpr();
            for (int i = 0; i < n; i++) {
                constraint.addTerm(items.get(i).getWeight(), x[i][j]);
            }
            model.addConstr(constraint, GRB.LESS_EQUAL, , "ASS_" + i);
        }
        return false;

    }
}

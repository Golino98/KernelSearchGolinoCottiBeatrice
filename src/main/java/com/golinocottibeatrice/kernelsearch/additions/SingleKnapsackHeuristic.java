package com.golinocottibeatrice.kernelsearch.additions;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.instance.Item;
import com.golinocottibeatrice.kernelsearch.instance.Knapsack;
import com.golinocottibeatrice.kernelsearch.solver.Variable;
import gurobi.*;

import java.util.*;

/**
 * Rappresenta il seguente algoritmo euristico eseguibile su un'istanza MKP:
 * per ogni knapsack i dell'istanza, risolve un 0-1 knapsack problem,
 * massimizzando il profitto degli item inseriti in i.
 * Ad ogni iterazione vengono eliminati dalla lista di item quelli inseriti nel
 * knapsack, in modo tale che un item venga inserito al massimo in un knapsack.
 */
public class SingleKnapsackHeuristic {
    private final List<Knapsack> knapsacks;
    private final List<Item> items;
    private final GRBEnv env;

    /**
     * Costruisce una nuova istanza dell'euristica.
     *
     * @param instance L'istanza su applicare l'euristica.
     */
    public SingleKnapsackHeuristic(Instance instance, GRBEnv env) {
        this.knapsacks = instance.getKnapsacks();
        // Crea una nuova lista per evitare di cancellare item dalla lista originale
        this.items = new ArrayList<>(instance.getItems());
        this.env = env;
    }

    public List<Variable> run() throws GRBException {
        var variables = new ArrayList<Variable>();

        for (var knapsack : knapsacks) {
            var model = new GRBModel(env);
            var x = new HashMap<Item, GRBVar>();

            for (var j : items) {
                x.put(j, model.addVar(0, 1, 0, GRB.BINARY, "X_" + j.getIndex()));
            }

            var ctr = new GRBLinExpr();
            for (var j : items) {
                ctr.addTerm(j.getWeight(), x.get(j));
            }
            model.addConstr(ctr, GRB.LESS_EQUAL, knapsack.getCapacity(), "CAPACITY");

            var obj = new GRBLinExpr();
            for (var j : items) {
                obj.addTerm(j.getProfit(), x.get(j));
            }
            model.setObjective(obj, GRB.MAXIMIZE);

            model.optimize();

            for (var item : x.keySet()) {
                var value = x.get(item).get(GRB.DoubleAttr.X);
                var name = String.format("x_%d_%d", knapsack.getIndex(), item.getIndex());
                variables.add(new Variable(name, item, knapsack, value));

                if (value == 1) {
                    items.remove(item);
                }
            }
        }

        return variables;
    }
}

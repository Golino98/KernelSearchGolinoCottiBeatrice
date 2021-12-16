package com.golinocottibeatrice.kernelsearch.solver;

/**
 * Rappresenta una variabile x(i,j) del problema.
 */
public class Variable {
    private final String name;
    private final double value;
    private final double rc;
    private final int weight;
    private final int profit;

    /**
     * Crea una nuova variabile.
     *
     * @param name  Il nome della variabile.
     * @param value Il valore assegnato alla variabile (non necessariamente binario).
     * @param rc    Il valore del costo ridotto (diverso da zero solo se rilassamento continuo).
     */
    Variable(String name, double value, double rc, int weight, int profit) {
        this.name = name;
        this.value = value;
        this.rc = rc;
        this.weight = weight;
        this.profit = profit;

    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public int getProfit() {
        return profit;
    }


    /**
     * Restituisce il valore del costo ridotto assegnato alla variabile.
     *
     * @return Il costo ridotto, diverso da zero solo se rilassamento continuo.
     */
    public double getRC() {
        return rc;
    }

    public double getProfitDividedByWeight() {
        return this.profit / this.weight;
    }
}

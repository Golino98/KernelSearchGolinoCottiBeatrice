package com.golino.cotti.classes.solver;

/**
 * Rappresenta una variabile x(i,j) del problema.
 */
public class Variable {
    private final String name;
    private final double value;
    private final double rc;

    /**
     * Crea una nuova variabile.
     *
     * @param name  Il nome della variabile.
     * @param value Il valore assegnato alla variabile (non necessariamente binario).
     * @param rc    Il valore del costo ridotto (diverso da zero solo se rilassamento continuo).
     */
    public Variable(String name, double value, double rc) {
        this.name = name;
        this.value = value;
        this.rc = rc;
    }

    /**
     * Crea una nuova variabile.
     *
     * @param name  Il nome della variabile.
     * @param value Il valore assegnato alla variabile (non necessariamente binario).
     */
    public Variable(String name, double value) {
        this(name, value, 0);
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    /**
     * Restituisce il valore del costo ridotto assegnato alla variabile.
     *
     * @return Il costo ridotto, diverso da zero solo se rilassamento continuo.
     */
    public double getRC() {
        return rc;
    }
}

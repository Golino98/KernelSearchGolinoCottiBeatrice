package com.golinocottibeatrice.kernelsearch.solver;

import java.util.ArrayList;

/**
 * Rappresenta una soluzione vuota del problema,
 * che quindi ha valore della funzione obiettivo uguale a 0
 * e lista delle variabili vuota.
 */
class EmptySolution extends Solution {
    EmptySolution() {
        super(0,new ArrayList<>());
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}

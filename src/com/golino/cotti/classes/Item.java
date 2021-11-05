package com.golino.cotti.classes;

/**
 * Rappresenta un elemento del problema da risolvere.
 */
public class Item {
    private final String name;
    private final double rc;
    private final double xr;

    private final int weight;
    private final int profit;

    /**
     * Metodo costruttore della classe {@link Item}
     *
     * @param name, stringa che verrà assegnata al nome del nuovo item
     * @param xr
     * @param rc
     */
    public Item(String name, double xr, double rc) {
        this.name = name;
        this.xr = xr;
        this.rc = rc;

        this.weight = 0;
        this.profit = 0;
    }

    /**
     * Metodo getters della variabile name
     *
     * @return la stringa contenente il valore della variabile name
     */
    public String getName() {
        return name;
    }

    public double getRc() {
        return rc;
    }

    public double getXr() {
        return xr;
    }
}
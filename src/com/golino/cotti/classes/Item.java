package com.golino.cotti.classes;

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

    public int getWeight() {
        return weight;
    }

    public int getProfit() {
        return profit;
    }

    public Item(int weight, int profit)
    {
        this.name=null;
        this.rc = 0;
        this.xr = 0;
        this.weight = weight;
        this.profit = profit;
    }

    /**
     * Metodo getters della variabile name
     *
     * @return la stringa contenente il valore della variabile name
     */
    public String getName() {
        return name;
    }

    /**
     * Metodo getters della variabile rc
     *
     * @return un double contentente il valore della variabile rc
     */
    public double getRc() {
        return rc;
    }

    /**
     * Metodo getters della variabile xr
     *
     * @return un double contentente il valore della variabile xr
     */
    public double getXr() {
        return xr;
    }

    /**
     * @return il valore assoluto della variabile rc
     */
    public double getAbsoluteRC() {
        return Math.abs(rc);
    }
}
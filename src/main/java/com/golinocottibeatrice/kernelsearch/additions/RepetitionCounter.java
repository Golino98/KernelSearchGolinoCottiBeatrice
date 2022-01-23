package com.golinocottibeatrice.kernelsearch.additions;

/**
 * Rappresenta un counter che, quando il counter raggiunge il valore {@code h},
 * assume valore {@code true} per i successivi {@code k} incrementi.
 * In tutti gli altri casi resituisce il counter assume valore {@code false}.
 * <p>
 * Lo stato {@code true} o {@code false} del counter può essere verificato con il metodo {@code check()}.
 */
public class RepetitionCounter {
    private static final String INVALID_PARAMETERS = "Invalid value for h or k: they must both be >= 0";

    private final int h;
    private final int k;

    private int counter;
    private int resetCounter;

    /**
     * Costruisce un nuovo counter.
     *
     * @param h Il numero di incrementi dopo il cui restiuire {@code true} per k iterazioni.
     * @param k Il numero di volte in cui restituire {@code true}.
     */
    public RepetitionCounter(int h, int k) {
        if (h < 0 || k < 0) {
            throw new IllegalArgumentException(INVALID_PARAMETERS);
        }

        this.h = h;
        this.k = k;
    }

    /**
     * Incrementa il counter.
     */
    public void increment() {
        if (resetCounter > 0) {
            resetCounter--;
            counter = 0;
            return;
        }

        counter++;
        if (counter >= h) {
            resetCounter = k;
        }
    }

    /**
     * Controlla il valore booleano del counter.
     *
     * @return {@code true} se sono stati raggiunti h incrementi, {@code false} altrimenti
     */
    public boolean check() {
        return resetCounter > 0;
    }

    /**
     * Resetta il counter.
     */
    public void reset() {
        counter = 0;
        resetCounter = 0;
    }

    public int getH() {
        return h;
    }

    public int getK() {
        return k;
    }
}

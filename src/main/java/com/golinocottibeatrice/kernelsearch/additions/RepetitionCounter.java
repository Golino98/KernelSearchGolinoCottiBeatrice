package com.golinocottibeatrice.kernelsearch.additions;

/**
 * Rappresenta un counter che viene incrementato ogni volta che
 * riceve in input un valore uguale al precedente.
 * <p>
 * Quando il counter raggiunge il valore {@code h},
 * ovvero riceve in input {@code h} volte lo stesso valore,
 * restituisce il valore {@code true} per le successive {@code k} chiamate.
 * In tutti gli altri casi resituisce il valore {@code false}.
 * <p>
 * Il counter viene resettato quando riceve in input un valore
 * diverso dai precedenti, oppure dopo le {@code k} volte in
 * cui restituisce {@code true}.
 * <p>
 * Durante le {@code k} iterazioni in cui restituisce {@code true},
 * non viene fatto nessun controllo sui valori che riceve in input,
 * ma viene resituito {@code true} ciecamente.
 * Il valore dell'ultimo input ricevuto viene resettato una volta passate le
 * {@code k} iterazioni.
 */
public class RepetitionCounter {
    private static final String INVALID_PARAMETERS = "Invalid value for h or k: they must both be > 0";
    private final int h;
    private final int k;
    private final double initial;

    private int counter = 1;
    private int resetCounter;
    private double lastValue;

    /**
     * Costruisce un nuovo counter.
     *
     * @param h       Il numero di input uguali dopo il cui restiuire {@code true} per k iterazioni.
     * @param k       Il numero di volte in cui restituire {@code true}.
     * @param initial Il valore iniziale da tenere in memoria.
     */
    public RepetitionCounter(int h, int k, double initial) {
        if (h <= 0 || k <= 0) {
            throw new IllegalArgumentException(INVALID_PARAMETERS);
        }
        this.h = h;
        this.k = k;
        this.initial = initial;
        this.lastValue = initial;
    }

    /**
     * Inserisce un nuovo valore nel counter.
     *
     * @param value Il valore da confrontare con il precedente dato in input.
     * @return {@code true} per {@code k} volte se viene dato in input lo stesso valore per {@code h} volte.
     */
    public boolean value(double value) {
        if (resetCounter > 0) {
            resetCounter--;
            counter = 1;
            lastValue = initial;
            return true;
        }

        if (value == lastValue) {
            counter++;
        } else {
            counter = 1;
        }

        if (counter >= h) {
            resetCounter = k;
        }

        lastValue = value;
        return counter > h;
    }

    /**
     * Resetta il counter.
     */
    public void reset() {
        counter = 1;
        resetCounter = 0;
        lastValue = initial;
    }
}

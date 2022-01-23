package com.golinocottibeatrice.kernelsearch.additions;

/**
 * Rappresenta un repetition counter disabilitato, ovvero che non fa assolutamente nulla.
 */
public class DisabledRepetitionCounter extends RepetitionCounter {
    /**
     * Costruisce un nuovo counter che non fa nulla.
     */
    public DisabledRepetitionCounter() {
        super(0, 0);
    }

    @Override
    public void increment() {
    }

    /**
     * Controlla il valore del counter.
     *
     * @return Sempre e comunque {@code false}.
     */
    @Override
    public boolean check() {
        return false;
    }

    @Override
    public void reset() {
    }
}

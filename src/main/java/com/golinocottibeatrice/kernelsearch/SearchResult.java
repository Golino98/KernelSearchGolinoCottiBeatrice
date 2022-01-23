package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Solution;

/**
 * Rappresenta il risultato ottenuto dalla kernel search.
 */
public class SearchResult {
    private final Solution solution;
    private final double timeElapsed;
    private final boolean timeLimitReached;

    /**
     * Costruisce un nuovo SearchResult.
     *
     * @param solution         La soluzione trovata.
     * @param timeElapsed      Il tempo impiegato.
     * @param timeLimitReached Valore booleano che indica se il tempo limite è stato raggiunto.
     */
    public SearchResult(Solution solution, double timeElapsed, boolean timeLimitReached) {
        this.solution = solution;
        this.timeElapsed = timeElapsed;
        this.timeLimitReached = timeLimitReached;
    }

    public int getObjective() {
        return (int) solution.getObjective();
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }

    public boolean timeLimitReached() {
        return timeLimitReached;
    }
}

package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Solution;

public class SearchResult {
    private final Solution solution;
    private final double timeElapsed;
    private final boolean timeLimitReached;

    public SearchResult(Solution solution, double timeElapsed, boolean timeLimitReached) {
        this.solution = solution;
        this.timeElapsed = timeElapsed;
        this.timeLimitReached=timeLimitReached;
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

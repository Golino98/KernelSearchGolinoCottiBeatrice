package com.golinocottibeatrice.kernelsearch.util;

public class Timer {
    private final long timeLimit;
    private long startTime;

    public Timer(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public double elapsedTime() {
        var time = (double) (System.nanoTime() - startTime);
        return time / 1_000_000_000;
    }

    // Restituisce il tempo rimamente per l'esecuzione
    public long getRemainingTime() {
        var time = timeLimit - (long) elapsedTime();
        return time >= 0 ? time : 0;
    }
}

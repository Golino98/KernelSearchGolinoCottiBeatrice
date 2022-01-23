package com.golinocottibeatrice.kernelsearch.util;

public class Timer {
    // Soglia tra il tempo trascorso e il tempo massimo di esecuzione
    // sotto il cui viene fermato il programma
    private static final int TIME_THRESHOLD = 2;

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

    public boolean timeLimitReached() {
        return getRemainingTime() <= TIME_THRESHOLD;
    }
}

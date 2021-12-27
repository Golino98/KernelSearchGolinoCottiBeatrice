package com.golinocottibeatrice.kernelsearch;

public class AcceptanceCounter {
    private final int counterThreshold;
    private final int resetThreshold;
    private int counter = 1;
    private int resetCounter;
    private double lastObj;

    public AcceptanceCounter(int counterThreshold, int resetThreshold) {
        this.counterThreshold = counterThreshold;
        this.resetThreshold = resetThreshold;
    }

    public boolean solution(double obj) {
        if (resetCounter > 0) {
            resetCounter--;
            counter = 1;
            lastObj = 0;
            return true;
        }

        if (obj == lastObj) {
            counter++;
        } else {
            counter = 1;
        }

        if (counter >= counterThreshold) {
            resetCounter = resetThreshold;
        }

        lastObj = obj;
        return counter > counterThreshold;
    }
}

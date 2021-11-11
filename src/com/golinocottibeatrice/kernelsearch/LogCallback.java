package com.golinocottibeatrice.kernelsearch;

import gurobi.GRB;
import gurobi.GRBCallback;
import gurobi.GRBException;

import java.time.Duration;
import java.time.Instant;

public class LogCallback extends GRBCallback {
    private static final String FORMAT_NEW_SOLUTION = "OBJ=%06.2f  -  TIME: +%ds";
    private static final String FORMAT_NEW_OPTIMUM = "OBJ=%06.2f* -  TIME: +%ds";
    private final Instant startTime;
    private double bestObj = 0;

    public LogCallback(Instant startTime) {
        this.startTime = startTime;
    }

    protected void callback() {
        try {
            if (where == GRB.CB_MIPSOL) {
                var obj = getDoubleInfo(GRB.CB_MIPSOL_OBJ);
                var time = Duration.between(startTime, Instant.now()).getSeconds();

                var format = FORMAT_NEW_SOLUTION;
                if (obj > bestObj) {
                    bestObj = obj;
                    format = FORMAT_NEW_OPTIMUM;
                }
                var log = String.format(format, obj, time);

                System.out.println(log);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
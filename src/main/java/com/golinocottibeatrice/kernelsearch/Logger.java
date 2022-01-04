package com.golinocottibeatrice.kernelsearch;

import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneOffset;

public class Logger {
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String BLUE = "\u001B[34m";

    private static final String SEPARATOR = "\n--------------------------------------------";
    private static final String INSTANCE = CYAN + "\nInstance:     " + RESET + "%s";
    private static final String START_TIME = CYAN + "\nStart time:   " + RESET + "%02d:%02d:%02d UTC";
    private static final String EJECT_ENABLED = CYAN + "\nKernel eject: " + RESET + "enabled (thld=%d)";
    private static final String EJECT_DISABLED = CYAN + "\nkernel eject: " + RESET + "disabled";
    private static final String REP_ENABLED = CYAN + "\nRep. counter: " + RESET + "enabled (thld=%d,pers=%d)";
    private static final String REP_DISABLED = CYAN + "\nRep. counter: " + RESET + "disabled";
    private static final String ITEMDOM_ENABLED = CYAN + "\nItem dom.   : " + RESET + "enabled";
    private static final String ITEMDOM_DISABLED = CYAN + "\nItem dom.    : " + RESET + "disabled";
    private static final String RELAXATION = PURPLE + "\n\n[Solving relaxation]\n" + RESET;
    private static final String KERNEL = PURPLE + "\n\n[Solving kernel - %d variables]\n" + RESET;
    private static final String ITERATION = PURPLE + "\n\n[Iteration %d]" + RESET;
    private static final String BUCKET = BLUE + "\n<Bucket %2d - %d variables> " + RESET;
    private static final String NEW_SOLUTION = "OBJ=%06.2f - TIME: +%fs";
    private static final String NEW_SOLUTION_SIZE = "SEL_VARS=%d (K_SIZE=%d) - OBJ=%06.2f - TIME: +%fs";
    private static final String NEW_SOLUTION_SIZE_EJECT = "SEL_VARS=%d - REM_VARS=%d - OBJ=%06.2f - TIME: +%fs - K_SIZE=%d";
    private static final String NO_SOLUTION = "NO SOLUTION  - TIME: +%fs";
    private static final String END = GREEN + "\n\nBest solution: " + RESET + "%06.2f\n" + GREEN + "Time elapsed:  " + RESET + "%fs\n";
    private static final String TIMELIMIT = YELLOW + "\n\nTime limit reached" + RESET;

    private final PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    public void start(String instance, Instant startTime) {
        var time = startTime.atZone(ZoneOffset.UTC).toLocalTime();

        out.print(SEPARATOR);
        out.printf(INSTANCE, instance);
        out.printf(START_TIME, time.getHour(), time.getMinute(), time.getSecond());
    }

    public void ejectStatus(boolean enabled, int threshold) {
        if (enabled) {
            out.printf(EJECT_ENABLED, threshold);
        } else {
            out.print(EJECT_DISABLED);
        }
    }

    public void repCtrStatus(boolean enabled, int threshold, int persistence) {
        if (enabled) {
            out.printf(REP_ENABLED, threshold, persistence);
        } else {
            out.print(REP_DISABLED);
        }
    }

    public void itemDomStatus(boolean enabled) {
        if (enabled) {
            out.print(ITEMDOM_ENABLED);
        } else {
            out.print(ITEMDOM_DISABLED);
        }
    }

    public void relaxStart() {
        out.print(RELAXATION);
    }

    public void kernelStart(int kernelSize) {
        out.printf(KERNEL, kernelSize);
    }

    public void solution(double objective, double elapsedTime) {
        out.printf(NEW_SOLUTION, objective, elapsedTime);
    }

    public void solution(int selectedVars, int kernelSize, double objective, double elapsedTime) {
        out.printf(NEW_SOLUTION_SIZE, selectedVars, kernelSize, objective, elapsedTime);
    }

    public void solution(int selectedVars, int kernelSize, double objective, double elapsedTime, int removedVars) {
        out.printf(NEW_SOLUTION_SIZE_EJECT, selectedVars, removedVars, objective, elapsedTime, kernelSize);
    }

    public void noSolution(double elapsedTime) {
        out.printf(NO_SOLUTION, elapsedTime);
    }

    public void iterationStart(int i) {
        out.printf(ITERATION, i);
    }

    public void bucketStart(int count, int bucketSize) {
        out.printf(BUCKET, count, bucketSize);
    }

    public void timeLimit() {
        out.print(TIMELIMIT);
    }

    public void end(double objective, double elapsedTime) {
        out.printf(END, objective, elapsedTime);
    }
}

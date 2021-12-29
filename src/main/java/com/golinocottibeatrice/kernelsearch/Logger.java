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
    private static final String FORMAT_START = CYAN + "\nInstance:     " + RESET + "%s\n" + CYAN + "Start time:   " + RESET + "%02d:%02d:%02d UTC";
    private static final String FORMAT_REP_EN = CYAN + "\nRep. counter: " + RESET + "enabled (h=%d,k=%d)\n";
    private static final String FORMAT_REP_DIS = CYAN + "\nRep. counter: " + RESET + "disabled\n";
    private static final String FORMAT_RELAX = PURPLE + "\n\n[Solving relaxation]\n" + RESET;
    private static final String FORMAT_KERNEL_SIZE = PURPLE + "\n\n[Solving kernel - %d variables]\n" + RESET;
    private static final String FORMAT_ITERATION = PURPLE + "\n\n[Iteration %d]" + RESET;
    private static final String FORMAT_SOLVE_BUCKET_SIZE = BLUE + "\n<Bucket %2d - %d variables> " + RESET;
    private static final String FORMAT_NEW_SOLUTION = "OBJ=%06.2f - TIME: +%fs";
    private static final String FORMAT_NEW_SOLUTION_SIZE = "SEL_VARS=%d (K_SIZE=%d) - OBJ=%06.2f - TIME: +%fs";
    private static final String FORMAT_NEW_SOLUTION_SIZE_EJECT = "SEL_VARS=%d - REM_VARS=%d - OBJ=%06.2f - TIME: +%fs - K_SIZE=%d";
    private static final String FORMAT_NO_SOLUTION_FOUND = "NO SOLUTION  - TIME: +%fs";
    private static final String FORMAT_END = GREEN + "\n\nBest solution: " + RESET + "%06.2f\n" + GREEN + "Time elapsed:  " + RESET + "%fs\n";
    private static final String TIMELIMIT = YELLOW + "\n\nTime limit reached" + RESET;

    private final PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    public void start(String instance, Instant startTime) {
        var time = startTime.atZone(ZoneOffset.UTC).toLocalTime();
        out.print(SEPARATOR);
        out.printf(FORMAT_START, instance, time.getHour(), time.getMinute(), time.getSecond());
    }

    public void repCtrStatus(boolean enabled, int h, int k) {
        if (enabled) {
            out.printf(FORMAT_REP_EN, h, k);
        } else {
            out.print(FORMAT_REP_DIS);
        }
    }

    public void relaxStart() {
        out.print(FORMAT_RELAX);
    }

    public void kernelStart(int kernelSize) {
        out.printf(FORMAT_KERNEL_SIZE, kernelSize);
    }

    public void solution(double objective, double elapsedTime) {
        out.printf(FORMAT_NEW_SOLUTION, objective, elapsedTime);
    }

    public void solution(int selected_variables, int kernelSize, double objective, double elapsedTime) {
        out.printf(FORMAT_NEW_SOLUTION_SIZE, selected_variables, kernelSize, objective, elapsedTime);
    }

    public void solution(int selected_variables, int kernelSize, double objective, double elapsedTime, int removed_vars) {
        out.printf(FORMAT_NEW_SOLUTION_SIZE_EJECT, selected_variables, removed_vars, objective, elapsedTime, kernelSize);
    }

    public void noSolution(double elapsedTime) {
        out.printf(FORMAT_NO_SOLUTION_FOUND, elapsedTime);
    }

    public void iterationStart(int i) {
        out.printf(FORMAT_ITERATION, i);
    }

    public void bucketStart(int count, int bucketSize) {
        out.printf(FORMAT_SOLVE_BUCKET_SIZE, count, bucketSize);
    }

    public void timeLimit() {
        out.print(TIMELIMIT);
    }

    public void end(double objective, double elapsedTime) {
        out.printf(FORMAT_END, objective, elapsedTime);
    }
}

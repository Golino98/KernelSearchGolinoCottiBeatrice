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
    private static final String FORMAT_START = CYAN + "\nInstance:   " + RESET + "%s\n" + CYAN + "Start time: " + RESET + "%02d:%02d:%02d UTC";
    private static final String FORMAT_RELAX = PURPLE + "\n\n[Solving relaxation]\n" + RESET;
    private static final String FORMAT_KERNEL = PURPLE + "\n\n[Solving kernel]\n" + RESET;
    private static final String FORMAT_ITERATION = PURPLE + "\n\n[Iteration %d]" + RESET;
    private static final String FORMAT_SOLVE_BUCKET = BLUE + "\n<Bucket %2d> " + RESET;
    private static final String FORMAT_NEW_SOLUTION = "OBJ=%06.2f - TIME: +%fs";
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

    public void relaxStart() {
        out.print(FORMAT_RELAX);
    }

    public void kernelStart() {
        out.print(FORMAT_KERNEL);
    }

    public void solution(double objective, double elapsedTime) {
        out.printf(FORMAT_NEW_SOLUTION, objective, elapsedTime);
    }

    public void noSolution(double elapsedTime) {
        out.printf(FORMAT_NO_SOLUTION_FOUND, elapsedTime);
    }

    public void iterationStart(int i) {
        out.printf(FORMAT_ITERATION, i);
    }

    public void bucketStart(int count) {
        out.printf(FORMAT_SOLVE_BUCKET, count);
    }

    public void timeLimit() {
        out.print(TIMELIMIT);
    }

    public void end(double objective, double elapsedTime) {
        out.printf(FORMAT_END, objective, elapsedTime);
    }
}

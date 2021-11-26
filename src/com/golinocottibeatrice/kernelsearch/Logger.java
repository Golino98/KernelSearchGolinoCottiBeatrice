package com.golinocottibeatrice.kernelsearch;

import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneOffset;

public class Logger {
    private static final String SEPARATOR = "\n--------------------------------------------";
    private static final String FORMAT_START = "\nInstance:   %s\nStart time: %02d:%02d:%02d UTC";
    private static final String FORMAT_RELAX = "\n\n[Solving relaxation]\n";
    private static final String FORMAT_KERNEL = "\n\n[Solving kernel]\n";
    private static final String FORMAT_ITERATION = "\n\n[Iteration %d]";
    private static final String FORMAT_SOLVE_BUCKET = "\n<Bucket %2d> ";
    private static final String FORMAT_NEW_SOLUTION = "OBJ=%06.2f - TIME: +%fs";
    private static final String FORMAT_NO_SOLUTION_FOUND = "NO SOLUTION  - TIME: +%fs";
    private static final String FORMAT_END = "\nBest solution: %06.2f\nTime elapsed:  %fs\n";
    private static final String TIMELIMIT = "\n\nTime limit reached";

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

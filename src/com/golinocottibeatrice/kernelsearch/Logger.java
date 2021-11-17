package com.golinocottibeatrice.kernelsearch;

import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Logger {
    private static final String FORMAT_START = "\nSTART TIME: %s:%s:%s";
    private static final String FORMAT_RELAX = "\n\n[Solving relaxation]\n";
    private static final String FORMAT_KERNEL = "\n\n[Solving kernel]\n";
    private static final String FORMAT_ITERATION = "\n\n[Iteration %d]\n";
    private static final String FORMAT_SOLVE_BUCKET = "\n<Bucket %d> ";
    private static final String FORMAT_NEW_SOLUTION = "OBJ=%06.2f - TIME: +%ds";
    private static final String NO_SOLUTION_FOUND = "No solution found";
    private final PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    public void start(Instant startTime) {
        var time = startTime.atZone(ZoneOffset.UTC).toLocalTime();
        out.printf(FORMAT_START, time.getHour(), time.getMinute(),time.getSecond());
    }

    public void relaxStart() {
        out.print(FORMAT_RELAX);
    }

    public void kernelStart() {
        out.print(FORMAT_KERNEL);
    }

    public void solution(double objective, int elapsedTime) {
        out.printf(FORMAT_NEW_SOLUTION, objective, elapsedTime);
    }

    public void noSolution() {
        out.print(NO_SOLUTION_FOUND);
    }

    public void iterationStart(int i) {
        out.printf(FORMAT_ITERATION, i);
    }

    public void bucketStart(int count) {
        out.printf(FORMAT_SOLVE_BUCKET, count);
    }
}

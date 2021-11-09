package com.golinocottibeatrice.kernelsearch;

import gurobi.GRB;
import gurobi.GRBCallback;
import gurobi.GRBException;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;

public class CustomCallback extends GRBCallback {
    private final String path;
    private PrintStream log;
    private final Instant startTime;
    private double bestObj;

    /**
     * Metodo costruttore della classe {@link CustomCallback}, sottoclasse di {@link GRBCallback}
     *
     * @param path
     * @param startTime
     */
    public CustomCallback(String path, Instant startTime) {
        this.path = path;
        this.startTime = startTime;
        bestObj = GRB.INFINITY;
        try {
            //autoFlush settato a true indica lo Stream viene eliminato automaticamente, senza bisogno di utilizzare successivamente il comando flush();
            this.log = new PrintStream(new BufferedOutputStream(new FileOutputStream(this.path + "\\" + "log_best_solutions.txt")), true);
            log.flush();
        } catch (FileNotFoundException e) {
            //Questa eccezione viene lanciata nel momento in cui il file non viene trovato o non è possibile crearlo
            e.printStackTrace();
        } catch (SecurityException e) {
            //Questa eccezione viene lanciata nel momento in cui non si hanno i diritti di accesso per il file su cui si ha intenzione di
            //andare a scrivere
            e.printStackTrace();
        }
    }

    /**
     *
     */
    protected void callback() {
        try {
            if (where == GRB.CB_MIPSOL) {
                double obj = getDoubleInfo(GRB.CB_MIPSOL_OBJ);

                if (obj < bestObj) {
                    bestObj = obj;
                    long time = Duration.between(startTime, Instant.now()).getSeconds();
                    log.println("OBJ:" + obj + "  -  TIME: " + time + " s.");
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
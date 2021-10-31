package com.golino.cotti.classes;

import gurobi.GRBException;

import java.io.IOException;
import java.util.List;

public class Start {
    public static void main(String[] args) {
        String instPath = args[0];
        String logPath = args[1];
        String configPath = args[2];

        try {
            var config = new ConfigurationReader(instPath,logPath,configPath).read();
            KernelSearch ks = new KernelSearch(config);
            ks.start();
            List<List<Double>> objValues = ks.getObjValues();
        } catch (IOException | GRBException e) {
            e.printStackTrace();
        }
    }
}

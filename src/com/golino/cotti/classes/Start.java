package com.golino.cotti.classes;

import gurobi.GRBException;

import java.io.IOException;
import java.util.List;

public class Start {
    public static void main(String[] args) {

        /**
         * @ configPath -> path per il file di configurazione config.txt (in cui sono presenti tutti i dati per il
         *                      settaggio del numero di bucket e così via)
         */
        /**
         * Da controllare
         */
        String instPath = "C:\\Users\\giaco\\IdeaProjects\\KernelSearchGolinoCotti\\istances\\test_res_395.txt";
        String logPath = "C:\\Users\\giaco\\IdeaProjects\\KernelSearchGolinoCotti\\log";
        String configPath = "C:\\Users\\giaco\\IdeaProjects\\KernelSearchGolinoCotti\\configurations\\config.txt";

        try {
            var config = new ConfigurationReader(instPath, logPath, configPath).read();
            KernelSearch ks = new KernelSearch(config);
            ks.start();
            List<List<Double>> objValues = ks.getObjValues();
        } catch (IOException | GRBException e) {
            e.printStackTrace();
        }
    }
}

package com.golino.cotti.classes;

import gurobi.GRBException;

import java.io.IOException;

public class Start {
    private static final String defaultConfigPath = "../configurations/config.txt";

    public static void main(String[] args) {
        // Path per il file di configurazione (in cui sono presenti tutti i dati per il
        // settaggio del numero di bucket e così via)
        String configPath = args[0];
        if (configPath == null || configPath.isEmpty()) {
            configPath = defaultConfigPath;
        }

        try {
            var config = new ConfigurationReader(configPath).read();
            KernelSearch ks = new KernelSearch(config);
            ks.start();
        } catch (IOException | GRBException e) {
            e.printStackTrace();
        }
    }
}

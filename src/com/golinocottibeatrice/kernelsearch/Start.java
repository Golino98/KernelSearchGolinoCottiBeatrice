package com.golinocottibeatrice.kernelsearch;

import gurobi.GRBException;

import java.io.IOException;

public class Start {
    // Path per il file di configurazione (in cui sono presenti tutti i dati per il
    // settaggio del numero di bucket e così via)
    private static final String defaultConfigPath = "./config.txt";

    public static void main(String[] args) {
        String configPath = defaultConfigPath;
        if (args.length != 0 && !args[0].isEmpty()) {
            configPath = args[0];
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

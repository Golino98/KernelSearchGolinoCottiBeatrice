package com.golinocottibeatrice.kernelsearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Lettore per la configurazione contenuta in un file txt.
 * Per informazioni sul formato del file di configurazione,
 * vedi la documentazione.
 */
public class ConfigurationReader {
    private static final String INVALID_CONFIGURATION_FORMAT = "Invalid configuration format.";
    private static final String UNRECOGNIZED_PARAMETER_NAME = "Unrecognized parameter name.";
    // Carattere usato per la separazione del nome del parametro dal valore
    private static final String SEPARATOR = "\\s+";

    private final String configPath;
    private final Configuration config = new Configuration();

    /**
     * Crea una nuova istanza di ConfigurationReader.
     *
     * @param configPath Il path del file di configurazione.
     */
    public ConfigurationReader(String configPath) {
        this.configPath = configPath;
    }

    /**
     * Legge il file di configurazione.
     *
     * @return Un'istanza di {@link Configuration} contenente la configurazione letta.
     * @throws IOException Errore nella lettura del file.
     */
    public Configuration read() throws IOException {
        var br = Files.newBufferedReader(Paths.get(configPath));
        var lines = br.lines().toList();

        for (var line : lines) {
            // Salta le linee vuote
            if (line.isEmpty()) {
                continue;
            }

            // Divide la stringa in un array di stringhe usando il separator
            var splitLine = line.split(SEPARATOR);

            if (splitLine.length != 2) {
                throw new IllegalStateException(INVALID_CONFIGURATION_FORMAT);
            }

            // La prima stringa è il nome dell'item di configurazione
            var item = splitLine[0];
            // La seconda stringa è il valore associato all'item
            var value = splitLine[1];

            switch (item) {
                case "THREADS" -> config.setNumThreads(Integer.parseInt(value));
                case "MIPGAP" -> config.setMipGap(Double.parseDouble(value));
                case "PRESOLVE" -> config.setPresolve(Integer.parseInt(value));
                case "TIMELIMIT" -> config.setTimeLimit(Integer.parseInt(value));
                case "SORTER" -> config.setVariableSorter(Integer.parseInt(value));
                case "KERNELSIZE" -> config.setKernelSize(Double.parseDouble(value));
                case "BUCKETSIZE" -> config.setBucketSize(Double.parseDouble(value));
                case "BUCKETBUILDER" -> config.setBucketBuilder(Integer.parseInt(value));
                case "TIMELIMITKERNEL" -> config.setTimeLimitKernel(Integer.parseInt(value));
                case "NUMITERATIONS" -> config.setNumIterations(Integer.parseInt(value));
                case "TIMELIMITBUCKET" -> config.setTimeLimitBucket(Integer.parseInt(value));
                case "KERNELBUILDER" -> config.setKernelBuilder(Integer.parseInt(value));
                case "INSTPATH" -> config.setInstPath(value);
                case "LOGDIR" -> config.setLogDir(value);
                case "RUNNAME" -> config.setRunName(value);

                case "ENABLE_EJECT" -> config.setEjectEnabled(Boolean.parseBoolean(value));
                case "EJECT_THRESHOLD" -> config.setEjectThreshold(Integer.parseInt(value));
                case "ENABLE_REPCTR" -> config.setRepCtrEnabled(Boolean.parseBoolean(value));
                case "REPCTR_THRESHOLD" -> config.setRepCtrThreshold(Integer.parseInt(value));
                case "REPCTR_PERSISTENCE" -> config.setRepCtrPersistence(Integer.parseInt(value));
                default -> throw new IllegalStateException(UNRECOGNIZED_PARAMETER_NAME);
            }
        }

        br.close();
        return config;
    }
}
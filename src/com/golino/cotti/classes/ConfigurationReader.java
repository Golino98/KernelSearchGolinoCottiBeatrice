package com.golino.cotti.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Lettore per la configurazione contenuta in un file txt.
 * Per informazioni sul formato del file di configurazione,
 * vedi /configurations/spiegazioneConfig.txt.
 */
public class ConfigurationReader {
    private final String configPath;
    private final Configuration config = new Configuration();
    // Carattere usato per la separazione del nome del parametro dal valore
    private static final String separator = "\\s+";

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
        BufferedReader br = Files.newBufferedReader(Paths.get(configPath));
        var lines = br.lines().collect(Collectors.toList());

        for (String line : lines) {
            // Divide la stringa in un array di stringhe usando il separator
            String[] splitLine = line.split(separator);
            // La prima stringa è il nome dell'item di configurazione
            var item = splitLine[0];
            // La seconda stringa è il valore associato all'item
            var value = splitLine[1];

            switch (item) {
                case "THREADS" -> config.setNumThreads(Integer.parseInt(value));
                case "MIPGAP" -> config.setMipGap(Double.parseDouble(value));
                case "PRESOLVE" -> config.setPresolve(Integer.parseInt(value));
                case "TIMELIMIT" -> config.setTimeLimit(Integer.parseInt(value));
                case "SORTER" -> {
                    if (Integer.parseInt(value) != 0) {
                        throw new IllegalStateException("Unrecognized item sorter.");
                    }
                    config.setItemSorter(new ItemSorterByValueAndAbsoluteRC());
                }
                case "KERNELSIZE" -> config.setKernelSize(Double.parseDouble(value));
                case "BUCKETSIZE" -> config.setBucketSize(Double.parseDouble(value));
                case "BUCKETBUILDER" -> {
                    if (Integer.parseInt(value) != 0) {
                        throw new IllegalStateException("Unrecognized bucket builder.");
                    }
                    config.setBucketBuilder(new DefaultBucketBuilder());
                }
                case "TIMELIMITKERNEL" -> config.setTimeLimitKernel(Integer.parseInt(value));
                case "NUMITERATIONS" -> config.setNumIterations(Integer.parseInt(value));
                case "TIMELIMITBUCKET" -> config.setTimeLimitBucket(Integer.parseInt(value));
                case "KERNELBUILDER" -> {
                    switch (Integer.parseInt(value)) {
                        case 0 -> config.setKernelBuilder(new KernelBuilderPositive());
                        case 1 -> config.setKernelBuilder(new KernelBuilderPercentage());
                        default -> throw new IllegalStateException("Unrecognized kernel builder.");
                    }
                }
                case "INSTPATH" -> config.setInstPath(value);
                case "LOGPATH" -> config.setLogPath(value);

                default -> System.out.println("Unrecognized parameter name.");
            }
        }

        return config;
    }
}
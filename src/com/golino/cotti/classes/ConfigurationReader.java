package com.golino.cotti.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationReader {

    /**
     * Metodo utilizzato inizialmente nel main per la creazione della configurazione.
     *
     * @param path di configurazione passato dal main
     * @return la variabile config di tipo {@link Configuration}
     * Questa configurazione viene genereata automaticamente grazie ad un insieme di switch vanno a leggere
     * il contenuto di un array di String contentente ogni parola contenuta nella variabile lines ({@link List<String>}
     */

    public static Configuration read(String path) {

        Configuration config = new Configuration();
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            lines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : lines) {

            //Comando che divide la stringa in un array di stringhe, il carattere utilizzato per la separazione è lo
            //lo spazio (anche multipli)
            String[] splitLine = line.split("\\s+");

            //Prendo la prima parola inserita nell'array e vado a settare la configurazione in base ai parametri (salvati
            //nella seconda cella dell'array)
            switch (splitLine[0]) {
                case "THREADS" -> config.setNumThreads(Integer.parseInt(splitLine[1]));
                case "MIPGAP" -> config.setMipGap(Double.parseDouble(splitLine[1]));
                case "PRESOLVE" -> config.setPresolve(Integer.parseInt(splitLine[1]));
                case "TIMELIMIT" -> config.setTimeLimit(Integer.parseInt(splitLine[1]));
                case "SORTER" -> {
                    if (Integer.parseInt(splitLine[1]) == 0) {
                        config.setItemSorter(new ItemSorterByValueAndAbsoluteRC());
                    } else {
                        System.out.println("Unrecognized item sorter.");
                    }
                }
                case "KERNELSIZE" -> config.setKernelSize(Double.parseDouble(splitLine[1]));
                case "BUCKETSIZE" -> config.setBucketSize(Double.parseDouble(splitLine[1]));
                case "BUCKETBUILDER" -> {
                    if (Integer.parseInt(splitLine[1]) == 0) {
                        config.setBucketBuilder(new DefaultBucketBuilder());
                    } else {
                        System.out.println("Unrecognized bucket builder.");
                    }
                }
                case "TIMELIMITKERNEL" -> config.setTimeLimitKernel(Integer.parseInt(splitLine[1]));
                case "NUMITERATIONS" -> config.setNumIterations(Integer.parseInt(splitLine[1]));
                case "TIMELIMITBUCKET" -> config.setTimeLimitBucket(Integer.parseInt(splitLine[1]));
                case "KERNELBUILDER" -> {
                    switch (Integer.parseInt(splitLine[1])) {
                        case 0 -> config.setKernelBuilder(new KernelBuilderPositive());
                        case 1 -> config.setKernelBuilder(new KernelBuilderPercentage());
                        default -> System.out.println("Unrecognized kernel builder.");
                    }
                }
                default -> System.out.println("Unrecognized parameter name.");
            }
        }
        return config;
    }
}
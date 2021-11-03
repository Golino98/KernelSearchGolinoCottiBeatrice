package com.golino.cotti.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationReader {
    private final String instPath;
    private final String logPath;
    private final String configPath;
    private final Configuration config = new Configuration();

    public ConfigurationReader(String instPath, String logPath, String configPath) {
        this.instPath = instPath;
        this.logPath = logPath;
        this.configPath = configPath;
    }

    /**
     * Metodo utilizzato inizialmente nel main per la creazione della configurazione.
     *
     * @return la variabile config di tipo {@link Configuration}
     * Questa configurazione viene genereata automaticamente grazie ad un insieme di switch vanno a leggere
     * il contenuto di un array di String contentente ogni parola contenuta nella variabile lines ({@link List<String>}
     */
    public Configuration read() throws IOException {
        BufferedReader br = Files.newBufferedReader(Paths.get(configPath));
        var lines = br.lines().collect(Collectors.toList());

        for (String line : lines) {
            //Comando che divide la stringa in un array di stringhe, il carattere utilizzato per la separazione è lo
            //lo spazio (anche multipli)
            String[] splitLine = line.split("\\s+");

            //Prendo la prima parola inserita nell'array e vado a settare la configurazione in base ai parametri (salvati
            //nella seconda cella dell'array)

            //Implemento una abstract class per il setting, estendo le classi per la configurazioni con un metodo per il settaggio
            //nel momento in cui vado a fare ad esempio un qualcosa come splitLine[0].set mi va a richiamare in automatico il metodo
            //senza dover andare a fare milioni di controlli
            switch (splitLine[0]) {
                case "THREADS" -> config.setNumThreads(Integer.parseInt(splitLine[1]));
                case "MIPGAP" -> config.setMipGap(Double.parseDouble(splitLine[1]));
                case "PRESOLVE" -> config.setPresolve(Integer.parseInt(splitLine[1]));
                case "TIMELIMIT" -> config.setTimeLimit(Integer.parseInt(splitLine[1]));
                case "SORTER" -> {
                    if (Integer.parseInt(splitLine[1]) != 0) {
                        throw new IllegalStateException("Unrecognized item sorter.");
                    }
                    config.setItemSorter(new ItemSorterByValueAndAbsoluteRC());
                }
                case "KERNELSIZE" -> config.setKernelSize(Double.parseDouble(splitLine[1]));
                case "BUCKETSIZE" -> config.setBucketSize(Double.parseDouble(splitLine[1]));
                case "BUCKETBUILDER" -> {
                    if (Integer.parseInt(splitLine[1]) != 0) {
                        throw new IllegalStateException("Unrecognized bucket builder.");
                    }
                    config.setBucketBuilder(new DefaultBucketBuilder());
                }
                case "TIMELIMITKERNEL" -> config.setTimeLimitKernel(Integer.parseInt(splitLine[1]));
                case "NUMITERATIONS" -> config.setNumIterations(Integer.parseInt(splitLine[1]));
                case "TIMELIMITBUCKET" -> config.setTimeLimitBucket(Integer.parseInt(splitLine[1]));
                case "KERNELBUILDER" -> {
                    switch (Integer.parseInt(splitLine[1])) {
                        case 0 -> config.setKernelBuilder(new KernelBuilderPositive());
                        case 1 -> config.setKernelBuilder(new KernelBuilderPercentage());
                        default -> throw new IllegalStateException("Unrecognized kernel builder.");
                    }
                }
                default -> System.out.println("Unrecognized parameter name.");
            }
        }

        config.setInstPath(instPath);
        config.setLogPath(logPath);
        return config;
    }
}
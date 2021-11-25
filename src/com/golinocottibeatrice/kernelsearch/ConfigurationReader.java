package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.instance.InstanceReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Lettore per la configurazione contenuta in un file txt.
 * Per informazioni sul formato del file di configurazione,
 * vedi la documentazione.
 */
public class ConfigurationReader {
    private static final String UNRECOGNIZED_KERNEL_BUILDER = "Unrecognized kernel builder.";
    private static final String UNRECOGNIZED_BUCKET_BUILDER = "Unrecognized bucket builder.";
    private static final String UNRECOGNIZED_ITEM_SORTER = "Unrecognized item sorter.";
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
        var lines = br.lines().collect(Collectors.toList());

        for (var line : lines) {
            // Divide la stringa in un array di stringhe usando il separator
            var splitLine = line.split(SEPARATOR);
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
                    switch(Integer.parseInt(value)) {
                        case 0 -> config.setVariableSorter(new VariableSorterByValueAndAbsoluteRC());
                        case 1 -> config.setVariableSorter(new VariableSorterByAbsoluteRCAndValue());
                        default -> throw new IllegalStateException(UNRECOGNIZED_ITEM_SORTER);
                    }
                    config.setVariableSorter(new VariableSorterByValueAndAbsoluteRC());
                }
                case "KERNELSIZE" -> config.setKernelSize(Double.parseDouble(value));
                case "BUCKETSIZE" -> config.setBucketSize(Double.parseDouble(value));
                case "BUCKETBUILDER" -> {
                    if (Integer.parseInt(value) != 0) {
                        throw new IllegalStateException(UNRECOGNIZED_BUCKET_BUILDER);
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
                        default -> throw new IllegalStateException(UNRECOGNIZED_KERNEL_BUILDER);
                    }
                }
                case "INSTPATH" -> {
                    var instances = new ArrayList<Instance>();

                    var inst = new File(value);
                    if (inst.isDirectory()) {
                        for (var instPath : Objects.requireNonNull(inst.listFiles())) {
                            instances.add(new InstanceReader(instPath.getAbsolutePath()).read());
                        }
                    } else {
                        instances.add(new InstanceReader(value).read());
                    }

                    config.setInstances(instances);
                }
                case "LOGDIR" -> {
                    Files.createDirectories(Paths.get(value));
                    config.setLogDir(value);
                }

                default -> System.out.println(UNRECOGNIZED_PARAMETER_NAME);
            }
        }
        config.setLogger(new Logger(System.out));
        return config;
    }
}
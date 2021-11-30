package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.instance.*;
import com.golinocottibeatrice.kernelsearch.search.*;
import com.golinocottibeatrice.kernelsearch.solver.*;
import com.golinocottibeatrice.kernelsearch.util.FileUtil;
import gurobi.GRBException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Gestisce l'avvio della kernel search.
 */
public class Starter {
    private static final String DEFAULT_CONFIG_PATH = "./config.txt";
    private static final String UNRECOGNIZED_KERNEL_BUILDER = "Unrecognized kernel builder.";
    private static final String UNRECOGNIZED_BUCKET_BUILDER = "Unrecognized bucket builder.";
    private static final String UNRECOGNIZED_ITEM_SORTER = "Unrecognized item sorter.";

    private final String configPath;
    private Configuration config;

    /**
     * Crea una nuova istanza di Starter con la configurazione letta da file.
     *
     * @param configPath Il path della configurazione.
     */
    public Starter(String configPath) {
        this.configPath = configPath.isEmpty() ? DEFAULT_CONFIG_PATH : configPath;
    }

    public static void main(String... args) {
        var configPath = "";
        // Verifica se il programma è stato avviato con un argomento,
        // che in tal caso corrisponderà al path della configurazione.
        if (args.length != 0 && !args[0].isEmpty()) {
            configPath = args[0];
        }

        try {
            new Starter(configPath).start();
        } catch (IOException | GRBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Legge la configurazione della Kernel Search,
     * crea le opportune dipendenze e infine avvia la kernel search.
     *
     * @throws IOException  Errore di lettura del file di configurazione o nella creazione della cartella di log.
     * @throws GRBException Errore di GUROBI.
     */
    public void start() throws IOException, GRBException {
        config = new ConfigurationReader(configPath).read();

        // Crea la cartella di log, se non esiste già.
        FileUtil.createDirectories(config.getLogDir());

        var solver = new Solver(buildSolverConfig());
        var searchConfig = buildSearchConfig();
        // Il solver viene settato qua perchè bisogna fare il dispose() alla fine dell'esecuzione.
        searchConfig.setSolver(solver);

        // Per ogni istanza, avvia una kernel search.
        for (var instance : getInstances()) {
            searchConfig.setInstance(instance);
            new KernelSearch(searchConfig).start();
        }

        // Libera le risorse usate dal solver.
        solver.dispose();
    }


    // Costruisce la configurazione del solver.
    private SolverConfiguration buildSolverConfig() {
        var solverConfig = new SolverConfiguration();

        solverConfig.setLogDir(config.getLogDir());
        solverConfig.setNumThreads(config.getNumThreads());
        solverConfig.setPresolve(config.getPresolve());
        solverConfig.setMipGap(config.getMipGap());

        return solverConfig;
    }

    // Crea la configurazione della Kernel Search sulla base della config letta da file.
    private SearchConfiguration buildSearchConfig() {
        var searchConfig = new SearchConfiguration();

        searchConfig.setLogger(new Logger(System.out));
        searchConfig.setTimeLimit(config.getTimeLimit());
        searchConfig.setTimeLimitKernel(config.getTimeLimitKernel());
        searchConfig.setTimeLimitBucket(config.getTimeLimitBucket());
        searchConfig.setNumIterations(config.getNumIterations());
        searchConfig.setKernelSize(config.getKernelSize());
        searchConfig.setBucketSize(config.getBucketSize());
        searchConfig.setVariableSorter(getVariableSorter());
        searchConfig.setBucketBuilder(getBucketBuilder());
        searchConfig.setKernelBuilder(getKernelBuilder());

        return searchConfig;
    }

    // Restituisce il sorter delle variabili selezionato nel file di config.
    private VariableSorter getVariableSorter() {
        return switch (config.getVariableSorter()) {
            case 0 -> new VariableSorterByValueAndAbsoluteRC();
            case 1 -> new VariableSorterByAbsoluteRCAndValue();
            default -> throw new IllegalStateException(UNRECOGNIZED_ITEM_SORTER);
        };
    }

    // Restituisce il builder dei bucket selezionato nel file di config.
    private BucketBuilder getBucketBuilder() {
        //noinspection SwitchStatementWithTooFewBranches
        return switch (config.getBucketBuilder()) {
            case 0 -> new DefaultBucketBuilder();
            default -> throw new IllegalStateException(UNRECOGNIZED_BUCKET_BUILDER);
        };
    }

    // Restituisce il builder del kernel selezionato nel file di config.
    private KernelBuilder getKernelBuilder() {
        return switch (config.getKernelBuilder()) {
            case 0 -> new KernelBuilderPositive();
            case 1 -> new KernelBuilderPercentage();
            default -> throw new IllegalStateException(UNRECOGNIZED_KERNEL_BUILDER);
        };
    }

    // Restituisce le istanze impostate nel file di config
    private List<Instance> getInstances() throws IOException {
        var instances = new ArrayList<Instance>();
        var inst = new File(config.getInstPath());

        // Se il path è una cartella aggiungi tutte le istanze contenute, altrimenti
        // aggiungi solo l'istanza singola.
        if (inst.isDirectory()) {
            for (var instPath : Objects.requireNonNull(inst.listFiles())) {
                instances.add(new InstanceReader(instPath.getAbsolutePath()).read());
            }
        } else {
            instances.add(new InstanceReader(config.getInstPath()).read());
        }

        return instances;
    }
}

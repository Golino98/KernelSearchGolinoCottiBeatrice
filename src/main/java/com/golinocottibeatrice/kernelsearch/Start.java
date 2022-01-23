package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.additions.DisabledRepetitionCounter;
import com.golinocottibeatrice.kernelsearch.additions.DominanceList;
import com.golinocottibeatrice.kernelsearch.additions.DominanceListBuilder;
import com.golinocottibeatrice.kernelsearch.additions.RepetitionCounter;
import com.golinocottibeatrice.kernelsearch.bucket.*;
import com.golinocottibeatrice.kernelsearch.instance.*;
import com.golinocottibeatrice.kernelsearch.kernel.*;
import com.golinocottibeatrice.kernelsearch.solver.*;
import com.golinocottibeatrice.kernelsearch.sorter.*;
import com.golinocottibeatrice.kernelsearch.util.FileUtil;
import gurobi.GRBException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce l'avvio della kernel search.
 */
public class Start {
    private static final String DEFAULT_CONFIG_PATH = "./config.txt";
    private static final String UNRECOGNIZED_KERNEL_BUILDER = "Unrecognized kernel builder.";
    private static final String UNRECOGNIZED_BUCKET_BUILDER = "Unrecognized bucket builder.";
    private static final String UNRECOGNIZED_ITEM_SORTER = "Unrecognized item sorter.";

    private final Configuration config;

    /**
     * Crea una nuova istanza di Starter con la configurazione letta da file.
     *
     * @param configPath Il path della configurazione.
     */
    public Start(String configPath) throws IOException {
        var path = configPath.isEmpty() ? DEFAULT_CONFIG_PATH : configPath;
        config = new ConfigurationReader(path).read();
    }

    public static void main(String... args) {
        var configPath = "";
        // Verifica se il programma è stato avviato con un argomento,
        // che in tal caso corrisponderà al path della configurazione.
        if (args.length != 0 && !args[0].isEmpty()) {
            configPath = args[0];
        }

        try {
            new Start(configPath).start();
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
        // Crea la cartella di log, se non esiste già.
        FileUtil.createDirectories(config.getLogDir());

        var searchConfig = buildSearchConfig();
        var printer = getPrinter();

        // Per ogni istanza, avvia una kernel search.
        for (var instance : getInstances()) {
            searchConfig.setInstance(instance);
            searchConfig.setDominanceList(getDominanceList(instance));

            var result = buildKernelSearch(searchConfig).start();

            printer.printRecord(instance.getName(), result.getObjective(),
                    result.getTimeElapsed(), result.timeLimitReached());
        }

        // Libera le risorse usate.
        searchConfig.getSolver().dispose();
        printer.close();
    }

    private KernelSearch buildKernelSearch(SearchConfiguration searchConfig) {
        return searchConfig.isEjectEnabled() ? new KernelSearchEject(searchConfig) : new KernelSearch(searchConfig);
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
    private SearchConfiguration buildSearchConfig() throws GRBException {
        SearchConfiguration searchConfig = new SearchConfiguration();

        searchConfig.setSolver(new Solver(buildSolverConfig()));
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
        searchConfig.setEjectEnabled(config.isEjectEnabled());
        searchConfig.setEjectThreshold(config.getEjectThreshold());
        searchConfig.setRepCtrEnabled(config.isRepCtrEnabled());
        searchConfig.setRepetitionCounter(getRepetitionCounter());
        searchConfig.setItemDomEnabled(config.isItemDomEnabled());

        return searchConfig;
    }

    // Restituisce il sorter delle variabili selezionato nel file di config.
    private VariableSorter getVariableSorter() {
        return switch (config.getVariableSorter()) {
            case 0 -> new VariableSorterByValueAndAbsoluteRC();
            case 1 -> new VariableSorterByAbsoluteRCAndValue();
            case 2 -> new VariableSorterByProfitDivideWeight();
            case 3 -> new VariableSorterRandom();
            case 4 -> new VariableSorterByValueProfitWeightAndRC();
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
            case 2 -> new KernelBuilderIntValues();
            default -> throw new IllegalStateException(UNRECOGNIZED_KERNEL_BUILDER);
        };
    }

    private CSVPrinter getPrinter() throws IOException {
        Writer out;
        if (config.getRunName().isEmpty()) {
            out = Writer.nullWriter();
        } else {
            out = new FileWriter(config.getLogDir() + "/" + config.getRunName() + ".csv");
        }

        return new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    private RepetitionCounter getRepetitionCounter() {
        if (config.isRepCtrEnabled()) {
            return new RepetitionCounter(config.getRepCtrThreshold(), config.getRepCtrPersistence());
        }
        return new DisabledRepetitionCounter();
    }

    private DominanceList getDominanceList(Instance instance) {
        if (config.isItemDomEnabled()) {
            return new DominanceListBuilder(instance).build();
        }

        return new DominanceList();
    }

    // Restituisce le istanze impostate nel file di config
    private List<Instance> getInstances() throws IOException {
        var instances = new ArrayList<Instance>();

        for (var file : FileUtil.getFiles(config.getInstPath())) {
            instances.add(new InstanceReader(file).read());
        }

        return instances;
    }
}

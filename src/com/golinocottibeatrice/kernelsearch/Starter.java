package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.solver.Solver;
import com.golinocottibeatrice.kernelsearch.solver.SolverConfiguration;
import gurobi.GRBException;

import java.io.IOException;

public class Starter {
    private static final String DEFAULT_CONFIG_PATH = "./config.txt";

    private final Configuration config;

    public Starter(String configPath) throws IOException {
        var path = configPath.isEmpty() ? DEFAULT_CONFIG_PATH : configPath;
        this.config = new ConfigurationReader(path).read();
    }

    public static void main(String[] args) {
        var configPath = "";
        if (args.length != 0 && !args[0].isEmpty()) {
            configPath = args[0];
        }

        try {
            new Starter(configPath).start();
        } catch (IOException | GRBException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException, GRBException {
        var searchConfig = new SearchConfiguration();
        var solver = new Solver(buildSolverConfig());

        searchConfig.setSolver(solver);
        searchConfig.setLogger(new Logger(System.out));
        searchConfig.setTimeLimit(config.getTimeLimit());
        searchConfig.setTimeLimitKernel(config.getTimeLimitKernel());
        searchConfig.setTimeLimitBucket(config.getTimeLimitBucket());
        searchConfig.setNumIterations(config.getNumIterations());
        searchConfig.setKernelSize(config.getKernelSize());
        searchConfig.setBucketSize(config.getBucketSize());
        searchConfig.setVariableSorter(config.getVariableSorter());
        searchConfig.setBucketBuilder(config.getBucketBuilder());
        searchConfig.setKernelBuilder(config.getKernelBuilder());

        for (var instance : config.getInstances()) {
            searchConfig.setInstance(instance);
            new KernelSearch(searchConfig).start();
        }

        solver.dispose();
    }

    private SolverConfiguration buildSolverConfig() {
        var solverConfig = new SolverConfiguration();
        solverConfig.setLogDir(config.getLogDir());
        solverConfig.setNumThreads(config.getNumThreads());
        solverConfig.setPresolve(config.getPresolve());
        solverConfig.setMipGap(config.getMipGap());
        return solverConfig;
    }
}

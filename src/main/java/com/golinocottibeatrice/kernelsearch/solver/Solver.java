package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.util.FileUtil;
import gurobi.GRBEnv;
import gurobi.GRBException;

/**
 * Risolutore per il problema della multiple knapsack,
 * che consente di creare dei modelli GUROBI
 * con dei valori di configurazione comuni.
 */
public class Solver {
    private final GRBEnv env;
    private final String logDir;

    /**
     * Crea un nuovo solver.
     *
     * @param env    Env di GUROBI.
     * @param logDir Cartella di log.
     */
    public Solver(GRBEnv env, String logDir) {
        this.env = env;
        this.logDir = logDir;
    }

    /**
     * Crea un nuovo modello.
     *
     * @param instance  L'istanza del problema MKP.
     * @param timeLimit Il limite di tempo per la risoluzione del problema.
     * @return Il modello creato.
     * @throws GRBException Errore di GUROBI.
     */
    public Model createModel(Instance instance, long timeLimit) throws GRBException {
        var config = getConfig(instance, timeLimit);
        return new ModelCreator(config).create();
    }

    /**
     * Crea un nuovo modello rilassato
     *
     * @param instance  L'istanza del problema MKP.
     * @param timeLimit Il limite di tempo per la risoluzione del problema.
     * @return Il modello creato.
     * @throws GRBException Errore di GUROBI.
     */
    public Model createRelaxed(Instance instance, long timeLimit) throws GRBException {
        var config = getConfig(instance, timeLimit);
        config.setLpRelaxation(true);
        return new ModelCreator(config).create();
    }

    private ModelConfiguration getConfig(Instance instance, long timeLimit) {
        var modelConfig = new ModelConfiguration();
        modelConfig.setEnv(env);
        modelConfig.setInstance(instance);
        modelConfig.setTimeLimit(timeLimit);
        modelConfig.setSolPath(FileUtil.getSolPath(logDir, instance.getName()));

        return modelConfig;
    }
}

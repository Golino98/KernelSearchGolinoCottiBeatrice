package com.golinocottibeatrice.kernelsearch.solver;

import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.util.FileUtil;
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;

/**
 * Risolutore per il problema della multiple knapsack,
 * che consente di creare dei modelli GUROBI
 * con dei valori di configurazione comuni.
 * <p>
 * Al termine dell'utilizzo il metodo <code>dispose()</code>
 * dovrebbe essere utilizzato per liberare le risorse.
 */
public class Solver {
    private final GRBEnv env;
    private final SolverConfiguration config;

    /**
     * Crea un nuovo solver.
     *
     * @param config La configurazione del solver.
     * @throws GRBException Errore di GUROBI.
     */
    public Solver(SolverConfiguration config) throws GRBException {
        this.config = config;
        this.env = new GRBEnv();
        env.set(GRB.IntParam.LogToConsole, 0);
        env.set(GRB.IntParam.Threads, config.getNumThreads());
        env.set(GRB.IntParam.Presolve, config.getPresolve());
        env.set(GRB.DoubleParam.MIPGap, config.getMipGap());
    }

    /**
     * Crea un nuovo modello.
     *
     * @param instance       L'istanza del problema MKP.
     * @param timeLimit      Il limite di tempo per la risoluzione del problema.
     * @param isLpRelaxation <code>true</code> se deve essere risolto il rilassato del problema.
     * @return Il modello creato.
     * @throws GRBException Errore di GUROBI.
     */
    public Model createModel(Instance instance, int timeLimit, boolean isLpRelaxation) throws GRBException {
        var modelConfig = new ModelConfiguration();
        modelConfig.setEnv(env);
        modelConfig.setInstance(instance);
        modelConfig.setTimeLimit(timeLimit);
        modelConfig.setLpRelaxation(isLpRelaxation);
        modelConfig.setLogPath(FileUtil.getLogPath(config.getLogDir(), instance.getName()));
        modelConfig.setSolPath(FileUtil.getSolPath(config.getLogDir(), instance.getName()));

        return new ModelCreator(modelConfig).create();
    }

    /**
     * Crea un nuovo modello NON rilassato.
     *
     * @param instance  L'istanza del problema MKP.
     * @param timeLimit Il limite di tempo per la risoluzione del problema.
     * @return Il modello creato.
     * @throws GRBException Errore di GUROBI.
     */
    public Model createModel(Instance instance, int timeLimit) throws GRBException {
        return createModel(instance, timeLimit, false);
    }

    /**
     * Libera le risorse usate dal solver.
     *
     * @throws GRBException Errore di GUROBI.
     */
    public void dispose() throws GRBException {
        env.dispose();
    }
}

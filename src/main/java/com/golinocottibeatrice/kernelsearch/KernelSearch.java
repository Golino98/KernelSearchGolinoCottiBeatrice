package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.additions.*;
import com.golinocottibeatrice.kernelsearch.bucket.Bucket;
import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.kernel.Kernel;
import com.golinocottibeatrice.kernelsearch.solver.*;
import com.golinocottibeatrice.kernelsearch.util.Timer;
import gurobi.GRBException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementa il metodo della kernel search, usando GUROBI come risolutore.
 * I parametri di configurazione vengono letti da un'istanza di {@link SearchConfiguration}.
 */
public class KernelSearch {
    // Soglia tra il tempo trascorso e il tempo massimo di esecuzione
    // sotto il cui viene fermato il programma
    protected static final int TIME_THRESHOLD = 2;

    protected final SearchConfiguration config;
    protected final Logger log;
    protected final Solver solver;
    protected List<Bucket> buckets;
    protected Kernel kernel;
    protected final Instance instance;
    protected final Timer timer;

    // Funzionalità aggiuntive
    private final RepetitionCounter repCtr;
    private final DominanceList dominanceList;

    // Variabili del problema
    private List<Variable> variables;
    // Miglior soluzione trovata
    private Solution bestSolution = new EmptySolution();
    protected long startTime;

    // La soluzione attuale, che potrebbe non essere la migliore trovata.
    private Solution currentSolution = new EmptySolution();

    /**
     * Crea una nuova istanza di kernel search.
     *
     * @param config La configurazione del metodo.
     */
    public KernelSearch(SearchConfiguration config) {
        this.config = config;
        instance = config.getInstance();
        log = config.getLogger();
        solver = config.getSolver();
        timer = new Timer(config.getTimeLimit());

        repCtr = config.getRepetitionCounter();
        dominanceList = config.getDominanceList();
    }

    /**
     * Avvia la kernel search.
     *
     * @throws GRBException Errore di Gurobi.
     */
    public SearchResult start() throws GRBException {
        timer.start();

        log.start(instance.getName());
        log.ejectStatus(config.isEjectEnabled(), config.getEjectThreshold());
        log.repCtrStatus(config.isRepCtrEnabled(), repCtr.getH(), repCtr.getK());
        log.itemDomStatus(config.isItemDomEnabled());
        log.heuristicStatus(config.isHeuristicEnabled());

        if (config.isHeuristicEnabled()) {
            runHeuristic();
        } else {
            solveRelaxation();
        }

        // Sorting delle variabili
        config.getVariableSorter().sort(variables);

        // Costruzione del kernel set
        kernel = config.getKernelBuilder().build(variables, config);
        kernel.getVariables().forEach(v -> v.setFromBucket(false));

        // Nei bucket vanno solo le variabili che non sono già nel kernel
        buckets = config.getBucketBuilder().build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);

        solveKernel();
        iterateBuckets();

        log.end(bestSolution.getObjective(), timer.elapsedTime());
        return new SearchResult(bestSolution, timer.elapsedTime(), timer.timeLimitReached());
    }

    // Risolve il rilassato dell'istanza
    private void solveRelaxation() throws GRBException {
        var model = solver.createRelaxed(instance, config.getTimeLimit());
        model.addDominanceConstraints(dominanceList);

        log.relaxStart();
        var solution = model.solve();
        log.solution(solution.getObjective(), timer.elapsedTime());

        variables = solution.getVariables();
        model.dispose();
    }

    // Esegue l'euristica
    private void runHeuristic() throws GRBException {
        log.heuristicStart();
        bestSolution = new SingleKnapsackHeuristic(instance, config.getGrbEnv()).run();
        log.solution(bestSolution.getObjective(), timer.elapsedTime());

        variables = bestSolution.getVariables();
    }

    // Risolve il kernel del problema
    private void solveKernel() throws GRBException {
        var timeLimit = Math.min(config.getTimeLimitKernel(), timer.getRemainingTime());
        var model = solver.createModel(instance, timeLimit);
        model.addDominanceConstraints(dominanceList);

        var toDisable = variables.stream().filter(v -> !kernel.contains(v)).toList();
        model.disableVariables(toDisable);

        log.kernelStart(kernel.size());
        currentSolution = model.solve();
        if (currentSolution.getObjective() >= bestSolution.getObjective()) {
            bestSolution = currentSolution;
        }
        log.solution(currentSolution.getObjective(), timer.elapsedTime());

        model.write();
        model.dispose();
    }

    protected void iterateBuckets() throws GRBException {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (timer.timeLimitReached()) {
                log.timeLimit();
                return;
            }

            log.iterationStart(i);

            solveBuckets();
        }
    }

    protected void solveBuckets() throws GRBException {
        int count = 0;

        currentSolution = bestSolution;

        for (var b : buckets) {
            if (timer.timeLimitReached()) {
                return;
            }

            count++;

            var model = this.buildModel(b, count);
            var solution = model.solve();
            // Se il valore trovato è uguale al precedente, incrementa counter
            if (solution.getObjective() == currentSolution.getObjective()) {
                repCtr.increment();
            }

            if (!solution.isEmpty()) {
                //countSolutions++;
                currentSolution = solution;
                if (solution.getObjective() > bestSolution.getObjective()) {
                    bestSolution = solution;
                    // Se viene trovato un ottimo globale, resetta il counter
                    repCtr.reset();
                }

                // Prendi le variabili del bucket che compaiono
                // nella nuova soluzione trovata e aggiungile al kernel
                var selected = model.getSelectedVariables(b.getVariables());

                this.placeInKernel(selected);

                this.executeEject(selected, solution);

                model.write();
            } else {
                log.noSolution(timer.elapsedTime());
            }

            model.dispose();
        }
    }

    protected void placeInKernel(List<Variable> selected) {
        selected.forEach(variable -> {
            if (!this.kernel.contains(variable))
                this.kernel.addItem(variable);
        });
    }

    protected void executeEject(List<Variable> selected, Solution solution) {
        log.solution(selected.size(), kernel.size(), solution.getObjective(), timer.elapsedTime());
    }

    private Model buildModel(Bucket b, int count) throws GRBException {
        log.bucketStart(count, b.size());

        var timeLimit = Math.min(config.getTimeLimitBucket(), timer.getRemainingTime());
        var model = solver.createModel(instance, timeLimit);

        model.addDominanceConstraints(dominanceList);

        // Disabilita le variabili che non sono ne nel kernel ne nel bucket
        var toDisable = variables.stream()
                .filter(v -> !kernel.contains(v) && !b.contains(v)).toList();
        model.disableVariables(toDisable);
        model.addBucketConstraint(b.getVariables());

        if (!currentSolution.isEmpty()) {
            // Vincolo di cutoff che impone che devo per forza avere una soluzione che migliora quella attuale.
            // Attivato solo se NON devo accettare tutte le soluzioni che trovo.
            if (!repCtr.check()) {
                model.addObjConstraint(currentSolution.getObjective());
            }
            // Imposta la soluzione da cui il modello parte.
            model.readSolution(currentSolution);
        }

        return model;
    }
}
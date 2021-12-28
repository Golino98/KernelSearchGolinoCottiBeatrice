package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.bucket.Bucket;
import com.golinocottibeatrice.kernelsearch.instance.Instance;
import com.golinocottibeatrice.kernelsearch.kernel.Kernel;
import com.golinocottibeatrice.kernelsearch.solver.*;
import gurobi.GRBException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementa il metodo della kernel search, usando GUROBI come risolutore.
 * I parametri di configurazione vengono letti da un'istanza di {@link SearchConfiguration}.
 */
public class KernelSearch {
    // Soglia tra il tempo trascorso e il tempo massimo di esecuzione
    // sotto il cui viene fermato il programma
    private static final int TIME_THRESHOLD = 2;

    private final SearchConfiguration config;
    private final Logger log;
    private final Solver solver;
    private List<Bucket> buckets;
    private Kernel kernel;

    private final Instance instance;
    private long startTime;
    // Variabili del problema
    private List<Variable> variables;
    // Miglior soluzione trovata
    private Solution bestSolution;

    // Funzionalità aggiuntive
    private RepetitionCounter repetitionCounter;

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

        if (config.isRepCtrEnabled()) {
            repetitionCounter = new RepetitionCounter(
                    config.getRepCtrThreshold(), config.getRepCtrPersistence(), -1);
        }
    }

    /**
     * Avvia la kernel search.
     *
     * @throws GRBException Errore di Gurobi.
     */
    public SearchResult start() throws GRBException {
        startTime = System.nanoTime();
        log.start(instance.getName(), Instant.now());
        log.repetitionCounterStatus(
                config.isRepCtrEnabled(), config.getRepCtrThreshold(), config.getRepCtrPersistence());

        solveRelaxation();

        config.getVariableSorter().sort(variables);

        kernel = config.getKernelBuilder().build(variables, config);
        // Nei bucket vanno solo le variabili che non sono già nel kernel
        buckets = config.getBucketBuilder().build(variables.stream()
                .filter(v -> !kernel.contains(v)).collect(Collectors.toList()), config);

        solveKernel();
        iterateBuckets();
        log.end(bestSolution.getObjective(), elapsedTime());

        return new SearchResult(bestSolution, elapsedTime(), elapsedTime() + TIME_THRESHOLD >= config.getTimeLimit());
    }

    private void solveRelaxation() throws GRBException {
        var model = solver.createModel(instance, config.getTimeLimit(), true);
        log.relaxStart();

        var solution = model.solve();
        log.solution(solution.getObjective(), elapsedTime());

        variables = solution.getVariables();
        model.dispose();
    }

    private void solveKernel() throws GRBException {
        var timeLimit = Math.min(config.getTimeLimitKernel(), getRemainingTime());
        var model = solver.createModel(instance, timeLimit);

        var toDisable = variables.stream().filter(v -> !kernel.contains(v)).collect(Collectors.toList());
        model.disableVariables(toDisable);

        log.kernelStart(kernel.size());
        bestSolution = model.solve();
        log.solution(bestSolution.getObjective(), elapsedTime());

        model.write();
        model.dispose();
    }

    private void iterateBuckets() throws GRBException {
        for (int i = 0; i < config.getNumIterations(); i++) {
            if (getRemainingTime() <= TIME_THRESHOLD) {
                log.timeLimit();
                return;
            }

            log.iterationStart(i);
            solveBuckets();
        }
    }

    private void solveBuckets() throws GRBException {
        int count = 0;
        // Se impostato a true, vengono accettate tutte le soluzioni,
        // anche quelle che peggiorano il valore della funzione obiettivo.
        // Sempre uguale a true se il counter delle ripetizioni è disabilitato.
        var disableCutoff = !config.isRepCtrEnabled();
        // La soluzione attuale, che potrebbe non essere la migliore trovata.
        var currentSolution = bestSolution;

        for (var b : buckets) {
            log.bucketStart(count, b.size());
            count++;

            var timeLimit = Math.min(config.getTimeLimitBucket(), getRemainingTime());
            var model = solver.createModel(instance, timeLimit);

            // Disabilita le variabili che non sono ne nel kernel ne nel bucket
            var toDisable = variables.stream()
                    .filter(v -> !kernel.contains(v) && !b.contains(v)).collect(Collectors.toList());
            model.disableVariables(toDisable);
            model.addBucketConstraint(b.getVariables());

            if (!currentSolution.isEmpty()) {
                // Vincolo di cutoff che impone che devo per forza avere una soluzione che migliora quella attuale.
                // Attivato solo se NON devo accettare tutte le soluzioni che trovo.
                if (!disableCutoff) {
                    model.addObjConstraint(currentSolution.getObjective());
                }
                // Imposta la soluzione da cui il modello parte.
                model.readSolution(currentSolution);
            }

            var solution = model.solve();
            if (config.isRepCtrEnabled()) {
                // Il counter mi dice se sto trovando ripetutamente lo stesso valore.
                // Se questo è il caso, nelle prossime iterazioni accetta qualsiasi soluzione
                // viene trovata, anche se non migliora la funzione obiettivo.
                disableCutoff = repetitionCounter.value(solution.getObjective());
            }

            if (!solution.isEmpty()) {
                currentSolution = solution;
                if (solution.getObjective() >= bestSolution.getObjective()) {
                    bestSolution = solution;
                }

                // Prendi le variabili del bucket che compaiono nella nuova soluzione trovata,
                // aggiungile al kernel, e rimuovile dal bucket
                var selected = model.getSelectedVariables(b.getVariables());
                selected.forEach(kernel::addItem);
                selected.forEach(b::removeItem);

                log.solution(selected.size(), kernel.size(), solution.getObjective(), elapsedTime());
                model.write();
            } else {
                log.noSolution(elapsedTime());
            }

            model.dispose();

            if (getRemainingTime() <= TIME_THRESHOLD) {
                return;
            }
        }
    }

    private double elapsedTime() {
        var time = (double) (System.nanoTime() - startTime);
        return time / 1_000_000_000;
    }

    // Restituisce il tempo rimamente per l'esecuzione
    private long getRemainingTime() {
        var time = config.getTimeLimit() - (long) elapsedTime();
        return time >= 0 ? time : 0;
    }
}
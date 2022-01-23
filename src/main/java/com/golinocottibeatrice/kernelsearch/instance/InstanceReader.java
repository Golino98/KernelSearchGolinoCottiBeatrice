package com.golinocottibeatrice.kernelsearch.instance;

import com.golinocottibeatrice.kernelsearch.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Lettore del file contenente l'istanza del problema multiple knapsack.
 * Il formato dei file d'istanza si trova nella documentazione del progetto.
 */
public class InstanceReader {
    // Carattere usato per separare weight e profit di un item
    private static final String SEPARATOR = "\\s+";

    private final String instPath;

    /**
     * Crea una nuovo InstanceReader.
     *
     * @param instPath Il path del file dell'istanza del problema.
     */
    public InstanceReader(String instPath) {
        this.instPath = instPath;
    }

    public InstanceReader(File inst) {
        this.instPath = inst.getAbsolutePath();
    }

    /**
     * Legge il file di istanza e crea la classe corrispondente.
     *
     * @return Un'istanza di {@link Instance} contenente l'istanza letta.
     * @throws IOException Errore nella lettura del file.
     */
    public Instance read() throws IOException {
        var path = Paths.get(instPath);

        // Nome dell'istanza
        var name = FileUtil.getFileName(path);

        var br = Files.newBufferedReader(path);
        var lines = br.lines().toList();

        // La prima linea contiene il numero di knapsacks
        var nKnapsacks = Integer.parseInt(lines.get(0));
        // La seconda linea contiene il numero di oggetti
        var nItems = Integer.parseInt(lines.get(1));

        var knapsacks = new ArrayList<Knapsack>(nKnapsacks);
        // Le righe dalla terza alla nKnapsacks+1 contengono le capacità
        // subList è inclusivo a destra e esclusivo a sinistra
        var knapsackLines = lines.subList(2, nKnapsacks + 2);
        for (var i = 0; i < knapsackLines.size(); i++) {
            knapsacks.add(new Knapsack(i, Integer.parseInt(knapsackLines.get(i))));
        }

        var items = new ArrayList<Item>(nItems);
        // Le righe dalla nKnapsacks+2 alla nItems+nKnapsacks+1 contengono gli oggetti
        var itemLines = lines.subList(nKnapsacks + 2, nItems + nKnapsacks + 2);
        for (var i = 0; i < itemLines.size(); i++) {
            var splitLine = itemLines.get(i).split(SEPARATOR);
            var weight = Integer.parseInt(splitLine[0]);
            var profit = Integer.parseInt(splitLine[1]);
            items.add(new Item(i, weight, profit));
        }

        br.close();
        return new Instance(name, knapsacks, items);
    }
}
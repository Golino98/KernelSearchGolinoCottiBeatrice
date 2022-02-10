package com.golinocottibeatrice.kernelsearch.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contiene metodi di utilità per operare su file.
 */
public class FileUtil {
    // Estensione dei file di soluzione
    private static final String SOL_EXT = ".sol";
    // Pattern per identificare la sola estensione di un file
    private static final String EXT_PATTERN = "(?<!^)[.].*";

    /**
     * Consente di ottenere il solo nome di un file, privato da ogni estensione.
     *
     * @param path Il file di cui ottenere il nome.
     * @return Il nome del file.
     */
    public static String getFileName(Path path, Path basePath) {
        if (path.toString().isEmpty()) {
            return "";
        }

        var relativePath = basePath.relativize(path);
        return relativePath.toString().replaceAll(EXT_PATTERN, "");
    }

    /**
     * Crea una cartella, creando anche tutte le directory "padre" che non esistono.
     *
     * @param path Il path della cartella da creare.
     * @throws IOException Errore di I/O.
     */
    public static void createDirectories(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
    }

    /**
     * Consente di comporre il path di un file di sol, unendo il path di una directory
     * al nome del file di sol, a cui viene aggiunta l'estensione appropriata.
     *
     * @param dir      La directory del file di sol.
     * @param fileName Il nome del file di sol.
     * @return Il path del file di sol.
     */
    public static String getSolPath(String dir, String fileName) {
        return Paths.get(dir, fileName + FileUtil.SOL_EXT).toString();
    }

    // Restituisce le istanze impostate nel file di config

    /**
     * Dato un path, restituisce il file associato al path,
     * se esso punta ad un file semplice.
     * Se invece punta a una cartella, restituisce i file contenuti
     * in essa, in modo non ricorsivo.
     *
     * @param path Il path in cui cercare file.
     * @return La lista dei file trovati.
     */
    public static List<File> getFiles(String path) {
        var filesList = new ArrayList<File>();
        var file = new File(path);

        // Se il path è una cartella aggiungi tutti i file contenuti, altrimenti
        // aggiungi solo il singolo file.
        if (file.isDirectory()) {
            for (var f : Objects.requireNonNull(file.listFiles())) {
                if (f.isDirectory()) {
                    filesList.addAll(getFiles(f));
                } else {
                    filesList.add(f);
                }
            }
        } else {
            filesList.add(file);
        }

        return filesList;
    }

    public static List<File> getFiles(File file) {
        return getFiles(file.getPath());
    }
}

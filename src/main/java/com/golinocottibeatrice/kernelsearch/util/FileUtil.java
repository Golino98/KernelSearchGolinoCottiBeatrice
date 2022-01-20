package com.golinocottibeatrice.kernelsearch.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public static String getFileName(String path) {
        if (path.isEmpty()) {
            return "";
        }

        return path.replaceAll(EXT_PATTERN, "");
    }

    /**
     * Consente di ottenere il solo nome di un file, privato da ogni estensione.
     *
     * @param path Il file di cui ottenere il nome.
     * @return Il nome del file.
     */
    public static String getFileName(Path path) {
        return getFileName(path.getFileName().toString());
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
}

package com.golinocottibeatrice.kernelsearch.util;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Contiene metodi di utilità per operare su file.
 */
public class FileUtil {
    // Estensione dei file di log
    private static final String LOG_EXT = ".log";
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
     * Consente di comporre il path di un file di log, unendo il path di una directory
     * al nome del file di log, a cui viene aggiunta l'estensione appropriata.
     *
     * @param dir      La directory del file di log.
     * @param fileName Il nome del file di log.
     * @return Il path del file di log.
     */
    public static String getLogPath(String dir, String fileName) {
        return getPath(dir, fileName, LOG_EXT);
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
        return getPath(dir, fileName, SOL_EXT);
    }

    /**
     * Consente di comporre il path di un file, unendo il path di una directory
     * al nome del file, a cui viene aggiunta un'estensione.
     *
     * @param dir      La directory del file.
     * @param fileName Il nome del file.
     * @param ext      L'estensione da dare al file.
     * @return Il path del file.
     */
    private static String getPath(String dir, String fileName, String ext) {
        return Paths.get(dir, fileName.concat(ext)).toString();
    }
}

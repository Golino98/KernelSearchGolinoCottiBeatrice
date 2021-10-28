package com.golino.cotti.classes;

import java.util.List;

public class Start {
    public static void main(String[] args) {

        String pathtxt = ".\\istances\\SMALL\\probT1_0U_R50_T002_M010_N0020_seed01.txt";
        //String pathmps = args[0];
        String pathlog = args[1];
        String pathConfig = args[2];
        Configuration config = ConfigurationReader.read(pathConfig);
        KernelSearch ks = new KernelSearch(pathtxt, pathlog, config);
        ks.start();

        List<List<Double>> objValues = ks.getObjValues();
    }
}

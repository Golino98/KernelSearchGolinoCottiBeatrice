package com.golino.cotti.classes;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    private double obj;
    private Map<String, Double> vars;
    private boolean empty;

    public Solution() {
        empty = true;
        vars = new HashMap<>();
    }

    public double getObj() {
        return obj;
    }

    public void setObj(double obj) {
        this.obj = obj;
    }

    public double getVarValue(String name) {
        return vars.get(name);
    }

    public void setVars(Map<String, Double> vars) {
        this.vars = vars;
        empty = false;
    }

    public boolean isEmpty() {
        return empty;
    }

}

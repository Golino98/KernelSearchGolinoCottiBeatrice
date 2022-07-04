package com.golinocottibeatrice.kernelsearch.sorter;

import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.Comparator;
import java.util.List;

public class VariableSorterByAbsoluteRCAndValue implements VariableSorter {


    public void sort(List<Variable> variables) {
        variables.sort(Comparator.comparing(Variable::getRC).reversed().thenComparing(Variable::getValue));
    }

    //Fatto in maniera sbagliata.
    //In questo modo le variabili vengono ordinate tramite i coefficienti di costo ridotto.
    //In caso di pari valore vengono ordinate tramite valore in maniera decrescente.
    /*
    @Override
    public void sort(List<Variable> variables) {
        variables.sort(Comparator.comparing(Variable::getRC).thenComparing(Variable::getValue).reversed());
    }*/
}

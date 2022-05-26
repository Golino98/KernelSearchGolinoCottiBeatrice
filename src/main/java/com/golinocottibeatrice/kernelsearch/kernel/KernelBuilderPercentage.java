package com.golinocottibeatrice.kernelsearch.kernel;

import com.golinocottibeatrice.kernelsearch.SearchConfiguration;
import com.golinocottibeatrice.kernelsearch.solver.Variable;

import java.util.List;

public class KernelBuilderPercentage implements KernelBuilder {
    @Override
    public Kernel build(List<Variable> variables, SearchConfiguration config) {

        //Creazione dell'insieme Kernel che verra' in cui verranno aggiunte le variabili
        Kernel kernel = new Kernel();

        //Ciclo "foreach" per effettuare il controllo su tutte le variabili del problema
        for (var v : variables)
        {
            //Controllo della condizione sulla dimenzione del Kernel
            if (kernel.size() < Math.round(config.getKernelSize() * variables.size()))
            {
                //Condizione verificata, aggiunta della variabile v nel Kernel
                kernel.addItem(v);
            } else
            {
                //Condizione non verificata. Break per passare alla prossima variabile
                break;
            }
        }
        //Viene restituito il Kernel in cui sono state inserite le variabili che soddisfano la condizione
        return kernel;
    }
}
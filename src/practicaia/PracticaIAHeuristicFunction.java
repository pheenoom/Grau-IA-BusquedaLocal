package practicaia;

import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Ruben Bagan Benavides, Marta Barroso Isidro, Gerard del Castillo Lite
 */
public class PracticaIAHeuristicFunction implements HeuristicFunction{
    
    @Override
    public double getHeuristicValue(Object o) {
        PracticaIAEstado estado = (PracticaIAEstado) o;
        
        // La primera version del heuristico calculara el coste en función unicamente
        // de la distancia.
        
        double h = 0.0;
        
        
        
        return h;        
    }

}

package practica.ia;

import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Ruben Bagan Benavides, Marta Barroso Isidro, Gerard del Castillo Lite
 */
public class HeuristicFunctionHC implements HeuristicFunction {
    private static EstadoHC estado;
            
    @Override
    public double getHeuristicValue(Object o) {        
        estado = (EstadoHC) o;    
        double miPutoHeuristico = 0.0;
        
        for (int i = 0; i < estado.getCentrosSize(); ++i) {
            
        }
        
        return miPutoHeuristico;
    }
}

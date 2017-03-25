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
        
        double coste = 0.0;
        for (Integer c : estado.getRedCentros().keySet()) {
            for (Integer s : estado.getRedCentros().get(c)) {
                coste += estado.getSensorCoste()[s];
                double aux = Math.pow(estado.getDistanciaSensorACentro(s, c - EstadoHC.NUM_SENSORES),2.0) * estado.getSensorDataOut()[s];
                coste += aux;
            }
        }
        
        return coste;
    }
}

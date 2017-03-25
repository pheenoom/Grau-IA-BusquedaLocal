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
        for (int c = 0; c < EstadoHC.NUM_CENTROS; c++) {
            for (Integer s : estado.getHijosCentro(c)) {
                coste += estado.getSensorCoste(s);
                coste += estado.getDistanciaSensorACentro(s, c) * estado.getSensorDataOut(s);
            }
            
        }
        
        return coste;
    }
}

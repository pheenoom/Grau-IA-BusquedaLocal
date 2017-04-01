package practica.ia;

import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Ruben Bagan Benavides, Marta Barroso Isidro, Gerard del Castillo Lite
 */
public class HeuristicFunctionHC implements HeuristicFunction {
    private static EstadoHC estado;
    private static double COSTE_PERDIDA = 0.0001; 

    
    @Override
    public double getHeuristicValue(Object o) {        
        estado = (EstadoHC) o;    
        
        double coste = 0.0;
        double perdidas = 0.0;
        for (int c = 0; c < EstadoHC.NUM_CENTROS; c++) {
            double dataIn = 0.0;
            for (Integer s : estado.getHijosCentro(c)) {
                dataIn += estado.getSensorDataOut(s);
                perdidas += estado.getSensorDataLoss(s);
                coste += estado.getSensorCoste(s);
                coste += estado.getDistanciaSensorACentro(s, c) 
                        * estado.getSensorDataOut(s);
            }
            if(dataIn > 150)
            {
                perdidas += dataIn-150;
            }
        }           
        
        double h = coste * COSTE_PERDIDA + perdidas * (1-COSTE_PERDIDA);        
        return h;
    }
}

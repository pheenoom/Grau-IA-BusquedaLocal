package practicaia;

import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Ruben Bagan Benavides, Marta Barroso Isidro, Gerard del Castillo Lite
 */
public class PracticaIAHeuristicFunction implements HeuristicFunction {
    private static PracticaIAEstado estado;
    private class VecCTP {
        public double c;
        public double t;
        public double p;
        
        public VecCTP() {
            this.c = this.t = this.p = 0.0;
        }
        
        public void suma(VecCTP v) {
            this.c += v.c;
            this.t += v.t;
            this.p += v.p;
        }
    }
    
   //el valor de transmitido es el volumen de datos transmitido por el nodo hijo
   void calcularTransmitido(int padre, VecCTP vecCTP){
        int capPadre = (int) estado.getSensores().get(padre).getCapacidad();
        if(vecCTP.t <= capPadre){
            vecCTP.t += capPadre * 1.5;
        }
        else{
            vecCTP.t = 1.5 * capPadre;
            vecCTP.p += vecCTP.t - capPadre;
        }

    }
    
    void recorrerSensores(int padre, VecCTP vecCTP){
        
        if(!estado.getRedSensores().get(padre).isEmpty()){ //hijos
            for(Integer hijo : estado.getRedSensores().get(padre)) {                
                recorrerSensores(hijo, vecCTP);
                double d = estado.getDistanciaEntreSensores(padre, hijo);
                vecCTP.c += Math.pow(d,2) * vecCTP.t;
                calcularTransmitido(padre, vecCTP);
            }
        }
        else {
            vecCTP.c = 0.0;
            vecCTP.p = 0.0;
            vecCTP.t = estado.getSensores().get(padre).getCapacidad() * 0.5;
        }
    }
    
    @Override
    public double getHeuristicValue(Object o) {        
        estado = (PracticaIAEstado) o;
        
        // La primera version del heuristico calculara el coste en función unicamente
        // de la distancia.
        double h;
        VecCTP global = new VecCTP();
        
        for (Integer centro : estado.getRedCentros().keySet()) {
            for (Integer sensor : estado.getRedCentros().get(centro)) {
                VecCTP vec = new VecCTP();
                recorrerSensores(sensor, vec);
                
                double d = estado.getDistanciaSensorACentro(sensor, centro);
                global.c = vec.c + Math.pow(d,2) * vec.t;
                global.p = global.p + vec.p;
                global.t = global.t + vec.t;
            }
            
            if(global.t >= 150){
                global.t = 150;
                global.p = global.t - 150;
            }            
        }
        
        return global.c;        
    }
}

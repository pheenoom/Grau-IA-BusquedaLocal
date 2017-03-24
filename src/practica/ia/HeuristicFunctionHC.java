package practica.ia;

import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Ruben Bagan Benavides, Marta Barroso Isidro, Gerard del Castillo Lite
 */
public class HeuristicFunctionHC implements HeuristicFunction {
    private static EstadoHC estado;
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
        if(vecCTP.t <= capPadre*2){
            vecCTP.t += capPadre;
        }
        else{
            vecCTP.t = 3* capPadre;
            vecCTP.p += vecCTP.t - capPadre;
        }

    }
    
    //pone en vecCTP la informacion del coste del arbol que empieza en 'raiz'
    void recorrerSensores(int raiz, VecCTP vecCTP){
        //Caso base; estamos en una hoja
        if(estado.getRedSensores().get(raiz).isEmpty()){ 
            vecCTP.c = 0.0;
            vecCTP.p = 0.0;
            vecCTP.t = estado.getSensores().get(raiz).getCapacidad();
        }
        else {
            //Caso recursivo: por cada hijo calculamos subarbol
            for(Integer hijo : estado.getRedSensores().get(raiz)) {                
                recorrerSensores(hijo, vecCTP);
                double d = estado.getDistanciaEntreSensores(raiz, hijo);
                vecCTP.c += Math.pow(d,2) * vecCTP.t;
                calcularTransmitido(raiz, vecCTP);
                
            }
        }
    }
        
    @Override
    public double getHeuristicValue(Object o) {        
        estado = (EstadoHC) o;
        
        // La primera version del heuristico calculara el coste en función unicamente
        // de la distancia.
        
        VecCTP datosGlobal = new VecCTP();
        
        for (Integer centro = 0; centro < estado.getCentrosSize(); centro++) {
            VecCTP datosCentro = new VecCTP();
            for (Integer sensor : estado.getHijosCentro(centro)) {
                VecCTP datosSubArbol = new VecCTP();
                recorrerSensores(sensor, datosSubArbol);
                
                double d = estado.getDistanciaSensorACentro(sensor, centro);
                double aux2 = Math.pow(d,2) * datosSubArbol.t;
                datosCentro.c += aux2;
                datosCentro.p += datosSubArbol.p;
                datosCentro.t += datosSubArbol.t;
                if(datosCentro.t >= 150)
                {
                    double aux = datosCentro.t;
                    datosCentro.t = 150;
                    datosCentro.p += aux - 150;
                }
                
            }
            
            datosGlobal.suma(datosCentro);
            
        }
        
        return datosGlobal.c;
    }
}

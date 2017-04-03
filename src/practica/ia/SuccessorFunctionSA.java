package practica.ia;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Ruben Bagan Benavides, Marta Barroso Isidro, Gerard del Castillo Lite
 */
public class SuccessorFunctionSA implements SuccessorFunction{

    private static double COSTE_PERDIDA = 0.0001; 
    
    @Override
    public List getSuccessors(Object o) {
        EstadoHC estado = (EstadoHC) o;
        ArrayList estadosGenerados = new ArrayList();
        
        boolean nuevoEstadoEncontrado = false;
        while (!nuevoEstadoEncontrado) {
            int indiceSensorOrigen = ThreadLocalRandom.current().nextInt(0, EstadoHC.NUM_SENSORES);
            int indiceSensorDestino = ThreadLocalRandom.current().nextInt(0, EstadoHC.NUM_SENSORES);
            int indiceCentroDestino = ThreadLocalRandom.current().nextInt(0, EstadoHC.NUM_CENTROS);
            
            if (ThreadLocalRandom.current().nextBoolean()) {
                if (estado.movimientoValido(indiceSensorOrigen, indiceSensorDestino)) 
                {                        
                    nuevoEstadoEncontrado = true;
                    EstadoHC nuevoEstadoMover = new EstadoHC(estado);
                    nuevoEstadoMover.mover(indiceSensorOrigen, indiceSensorDestino);
                    
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

                    estadosGenerados.add(new Successor("Conecto el Sensor " + (indiceSensorOrigen + 1) + " al sensor " + (indiceSensorDestino + 1), nuevoEstadoMover));
                }                                             
            }
            else {
                if (estado.movimientoValido(indiceSensorOrigen, estado.getCentro(indiceCentroDestino)))
                {
                    nuevoEstadoEncontrado = true;
                    EstadoHC nuevoEstado = new EstadoHC(estado);
                    nuevoEstado.mover(indiceSensorOrigen, estado.getCentro(indiceCentroDestino));
                    estadosGenerados.add(new Successor("Conecto el Sensor " + (indiceSensorOrigen + 1) + " al centro " + (indiceCentroDestino + 1), nuevoEstado));
                }
            }
        }
        
        return estadosGenerados;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package practica.ia;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ruben Bagan Benavides, Marta Barroso Isidro, Gerard del Castillo Lite
 */
public class SuccessorFunctionHC implements SuccessorFunction{

    @Override
    public List getSuccessors(Object o) {
        EstadoHC estado = (EstadoHC) o;
        ArrayList estadosGenerados = new ArrayList();
        byte S = 'S';
        byte C = 'C';
        for (int origen = 0; origen < EstadoHC.NUM_SENSORES; ++origen) {
            
            for (int destino = 0; destino < EstadoHC.NUM_SENSORES; ++destino) {
                if (destino != origen) 
                {
                    EstadoHC nuevoEstado = new EstadoHC(estado);
                
                    if (nuevoEstado.mover(origen, destino, S)) 
                    {
                        estadosGenerados.add(new Successor(
                                new String("Conecto el Sensor " + (origen + 1) + " al sensor " + (destino + 1)),
                                nuevoEstado));
                    }                    
                }                
            }
            
            for (int destino = 0; destino < EstadoHC.NUM_CENTROS; ++destino) {
                
                EstadoHC nuevoEstado = new EstadoHC(estado);
                if (nuevoEstado.mover(origen, destino, C)) 
                {
                    estadosGenerados.add(new Successor(
                            new String("Conecto el Sensor " + (origen + 1) + " al centro " + (destino + 1)),
                            nuevoEstado));
                }                
            }        
        }
        
        return estadosGenerados;
    }

}

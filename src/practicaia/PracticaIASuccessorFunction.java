/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package practicaia;

import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ruben Bagan Benavides, Marta Barroso Isidro, Gerard del Castillo Lite
 */
public class PracticaIASuccessorFunction implements SuccessorFunction{

    @Override
    public List getSuccessors(Object o) {
        PracticaIAEstado estado = (PracticaIAEstado) o;
        ArrayList<PracticaIAEstado> estadosGenerados = new ArrayList<>();
        /*
        for (int indiceSensor = 0; indiceSensor < PracticaIAEstado.getNUM_SENSORES(); ++indiceSensor) {
            
            for (int destino = 0; destino < PracticaIAEstado.getNUM_SENSORES(); ++destino) {
                if (destino != indiceSensor) {
                    PracticaIAEstado nuevoEstado = new PracticaIAEstado(estado);
                    if (nuevoEstado.mover(indiceSensor, destino)) {
                        estadosGenerados.add(nuevoEstado);
                    }                    
                }                
            }
            
            for (int destino = PracticaIAEstado.getNUM_SENSORES(); 
                    destino < PracticaIAEstado.getNUM_CENTROS() + PracticaIAEstado.getNUM_SENSORES(); 
                    ++destino) {
                PracticaIAEstado nuevoEstado = new PracticaIAEstado(estado);
                if (nuevoEstado.mover(indiceSensor, destino)) {
                    estadosGenerados.add(nuevoEstado);
                }                
            }            
        }*/
        
        return estadosGenerados;
    }

}

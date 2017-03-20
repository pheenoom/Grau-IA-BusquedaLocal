/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicaia;

import IA.Red.CentrosDatos;
import IA.Red.Sensores;
import java.util.HashMap;
import java.util.HashSet;

public class PracticaIA {
    public static void debug(PracticaIAEstado estado) {
        System.out.println("\t\t########################## Debug ##########################\n");
        estado.debugPrintMatrizDistancias();
        estado.debugPrintMatrizSensorACentro();
        estado.debugPrintSensores();
        estado.debugPrintCentros();
        estado.debugPrintRed();
        System.out.println("\n\t\t########################## Fi Debug ##########################");
    }
        
    public static void main(String[] args) {
        Sensores sensores = new Sensores(2, 43243244);
        CentrosDatos centrosDatos = new CentrosDatos(1, 435);
        
        PracticaIAEstado estado = new PracticaIAEstado(sensores, centrosDatos);
        estado.generarEstadoInicial();
        System.out.println("Distancia: " + estado.calcularDistanciaCentroIessimo(0));
        debug(estado);
 

        
    }    
}
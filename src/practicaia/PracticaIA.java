/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicaia;

import IA.Red.CentrosDatos;
import IA.Red.Sensores;

public class PracticaIA {
    public static void debug(PracticaIAEstado estado) {
        System.out.println("\t\t########################## Debug ##########################\n");
        estado.debugPrintMatrizDistancias();
        estado.debugPrintSensores();
        estado.debugPrintRed();
        System.out.println("\n\t\t########################## Fi Debug ##########################");
    }
        
    public static void main(String[] args) {
        Sensores sensores = new Sensores(100, 1234);
        CentrosDatos centrosDatos = new CentrosDatos(4, 1234);
        
        PracticaIAEstado estado = new PracticaIAEstado(sensores, centrosDatos);
        estado.generarEstadoInicial();
        debug(estado);
    }    
}
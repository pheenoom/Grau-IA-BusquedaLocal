/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicaia;

import IA.Red.Centro;
import IA.Red.CentrosDatos;
import IA.Red.Sensor;
import IA.Red.Sensores;
import java.util.ArrayList;

public class PracticaIA {
    public static void debug(PracticaIAEstado estado) {
        System.out.println("\t\t########################## Debug ##########################\n");
        estado.debugPrintMatrizDistancias();
        estado.debugPrintMatrizSensorACentro();
        estado.debugPrintSensores();
        estado.debugPrintCentros();
        estado.debugPrintRed();
        estado.debugPrintSensorDestino();
        System.out.println("\n\t\t########################## Fi Debug ##########################");
    }
        
    public static void main(String[] args) {
        //Sensores sensores = new Sensores(2, 43243244);
        //CentrosDatos centrosDatos = new CentrosDatos(1, 435);
        
        //PracticaIAEstado estado = new PracticaIAEstado(sensores, centrosDatos);
        //estado.generarEstadoInicial();
        //System.out.println("Distancia: " + estado.calcularDistanciaCentroIessimo(0));
        //debug(estado);
 
        // Bateria de pruebas de clase
        
        
        Sensor s1 = new Sensor(10, 3, 1);
        Sensor s2 = new Sensor(4, 6, 1);
        Sensor s3 = new Sensor(2, 8, 0);
        Sensor s4 = new Sensor(2, 8, 2);
        Sensor s5 = new Sensor(2, 10, 2);
        Sensor s6 = new Sensor(4, 5, 4);
        Sensor s7 = new Sensor(10, 0, 4);
        Sensor s8 = new Sensor(2, 7, 4);
        Sensor s9 = new Sensor(2, 9, 4);
        Sensor s10 = new Sensor(10, 5, 6);
        Sensor s11 = new Sensor(2, 2, 6);
        Sensor s12 = new Sensor(10, 9, 6);
        
        Centro c1 = new Centro(2, 5);
        Centro c2 = new Centro(7, 6);
        Centro c3 = new Centro(5, 3);
        
        Sensores sensores = new Sensores(0,1);
        CentrosDatos centrosDatos = new CentrosDatos(0,1);
        
        sensores.add(s1);
        sensores.add(s2);
        sensores.add(s3);
        sensores.add(s4);
        sensores.add(s5);
        sensores.add(s6);
        sensores.add(s7);
        sensores.add(s8);
        sensores.add(s9);
        sensores.add(s10);
        sensores.add(s11);
        sensores.add(s12);
        
        centrosDatos.add(c1);
        centrosDatos.add(c2);
        centrosDatos.add(c3);
        
        PracticaIAEstado estado = new PracticaIAEstado(sensores, centrosDatos);
        
        estado.generarEstadoInicial();        
        debug(estado);
        
        ArrayList<Boolean> ciclos = new ArrayList<>();
        for (int i = 0; i < PracticaIAEstado.NUM_SENSORES; ++i) {
            ciclos.add(estado.hayCiclos(i));
        }
        
        boolean hayCiclo = false;
        for (Boolean b : ciclos) {
            if (b) hayCiclo = true;
        }
        
        if (hayCiclo) {
            System.out.println("Hay un ciclo!");
        } 
        else {
            System.out.println("No hay ciclos!");
        }

        
    }    
}
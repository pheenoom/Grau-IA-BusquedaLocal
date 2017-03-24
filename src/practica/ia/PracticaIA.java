/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.ia;

import IA.Red.Centro;
import IA.Red.CentrosDatos;
import IA.Red.Sensor;
import IA.Red.Sensores;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PracticaIA {
    public static EstadoHC estado;
    public static Sensores sensores;
    public static CentrosDatos centrosDatos;
    
    ////////////////////////////////////////////////////////////////////////////
    ///                          Metodos DEBUG                               ///
    ////////////////////////////////////////////////////////////////////////////
    
    public static void debugPrintMatrizDistancias() {
        System.out.println();
        System.out.println("Matriz de distancias entre sensores: ");
        System.out.print("\t ");
        for (int i = 0; i < EstadoHC.NUM_SENSORES; ++i) {
            if ((i+1) > 9) {
                System.out.print("S" + (i+1) + "    ");
            }
            else 
            {
                System.out.print("S" + (i+1) + "     ");
            }
        }
        System.out.println();
        for (int i = 0; i < 100; ++i) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < EstadoHC.NUM_SENSORES; ++i) {
            System.out.print("S" + (i + 1) + "\t|");
            for (int j = 0; j < EstadoHC.NUM_SENSORES; ++j) {
                System.out.format("%.4f ", estado.getDistanciaEntreSensores(i, j));
            }
            System.out.println();
        }
    }
    
    public static void debugPrintMatrizSensorACentro() {   
        System.out.println();     
        System.out.println("Matriz de distancias sensores a centros: ");
        System.out.print("\t ");
        for (int i = 0; i < EstadoHC.NUM_CENTROS; ++i) {
            if ((i+1) > 9) {
                System.out.print("C" + (i+1) + "    ");
            }
            else 
            {
                System.out.print("C" + (i+1) + "     ");
            }
        }
        System.out.println();
        for (int i = 0; i < 100; ++i) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < EstadoHC.NUM_SENSORES; ++i) {
            System.out.print("S" + (i + 1) + "\t|");
            for (int j = 0; j < EstadoHC.NUM_CENTROS; ++j) {
                System.out.format("%.4f ", estado.getDistanciaSensorACentro(i, j));
            }
            System.out.println();
        }
    }
    
    public static void debugPrintSensores() {
        System.out.println();
        System.out.println("Informacion de los sensores: ");
        for (int i = 0; i < EstadoHC.NUM_SENSORES; ++i) {
            System.out.println("\tSensor " + (i + 1));
            System.out.println("\tCapacidad: " + sensores.get(i).getCapacidad());
            System.out.println("\tPosicion (X,Y): (" + 
                    sensores.get(i).getCoordX() + "," + 
                    sensores.get(i).getCoordY() + ")");
            System.out.println();
        }
    }
    
    public static void debugPrintCentros() {
        System.out.println();
        System.out.println("Informacion de los centros: ");
        for (int i = 0; i < EstadoHC.NUM_CENTROS; ++i) {
            System.out.println("\tCentro " + (i + 1));
            System.out.println("\tPosicion (X,Y): (" + 
                    centrosDatos.get(i).getCoordX() + "," + 
                    centrosDatos.get(i).getCoordY() + ")");
            System.out.println();
        }
    }
    
    public static void debugPrintRed_i(int indice, int tab) {
        for (int i = 0; i < tab; ++i) {
            System.out.print("\t");
        }
        
        HashMap<Integer, HashSet<Integer>> redSensores = estado.getRedSensores();
        System.out.println("Sensor " + (indice + 1) + ": ");
        for (Integer s : redSensores.get(indice)) {
            debugPrintRed_i(s,++tab);
        }
    }
    
    public static void debugPrintRed() {
        System.out.println();
        System.out.println("Informacion de la red: ");
        
        for (int i = 0; i < EstadoHC.NUM_CENTROS; ++i) {
            System.out.println("##############################################");
            System.out.println("Centro " + (i + 1) + ", tiene las siguientes conexiones: ");
            for (Integer s : estado.getHijosCentro(i)) {
                debugPrintRed_i(s, 1);
            }
            System.out.println("##############################################");
        }
    }
    
    public static void debugPrintSensorDestino() {
        System.out.println();
        System.out.println("Informacion de los nodos destino de los sensores");
        int[] nodoDestinoSensor = estado.getDestinos();
        byte[] tipoNodoDestinoSensor = estado.getTipos();
        for (int i = 0; i < EstadoHC.NUM_SENSORES; ++i) {
            System.out.print("S" + (i + 1) + " --> ");
            System.out.println(""+(char)tipoNodoDestinoSensor[nodoDestinoSensor[i]]
                     + (nodoDestinoSensor[i] + 1 - EstadoHC.NUM_SENSORES));
        }
    }
    
    
    public static void debug() {
        System.out.println("\t\t########################## Debug ##########################\n");
        debugPrintMatrizDistancias();
        debugPrintMatrizSensorACentro();
        debugPrintSensores();
        debugPrintCentros();
        debugPrintRed();
        debugPrintSensorDestino();
        System.out.println("\n\t\t########################## Fi Debug ##########################");
    }
    
    public static void debugHyaCiclos() {
        ArrayList<Boolean> ciclos = new ArrayList<>();
        
        for (int i = 0; i < EstadoHC.NUM_SENSORES; ++i) {
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
    
    
    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
    
    
    public static void main(String[] args) {
        /*
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
        
        sensores = new Sensores(0,1);
        centrosDatos = new CentrosDatos(0,1);
        
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
        
        // Ejemplo de prueba
        //estado = new EstadoHC(sensores, centrosDatos);
        //estado.generarEstadoInicial();
        
        // Ejemplo normal
        sensores = new Sensores(100, 1234);
        centrosDatos = new CentrosDatos(4, 1234);
        estado = new EstadoHC(sensores, centrosDatos);
        estado.generarEstadoInicial();
        
        
        debug();*/
        
        
        Sensor s1 = new Sensor(10, 99, 99);
        Sensor s2 = new Sensor(2, 100, 100);
        Centro c1 = new Centro(2, 5);
        sensores = new Sensores(0,1);
        centrosDatos = new CentrosDatos(0,1);
        sensores.add(s1);
        sensores.add(s2);
        centrosDatos.add(c1);
        estado = new EstadoHC(sensores, centrosDatos);
        estado.generarEstadoInicial();
        //estado.mover(0, 1);
        debug();
        
        Problem problem = new Problem(  estado, 
                                        new SuccessorFunctionHC(), 
                                        new GoalTestHC(),
                                        new HeuristicFunctionHC());
        
        HillClimbingSearch search = new HillClimbingSearch();
        try {
            SearchAgent agent = new SearchAgent(problem, search);  
            
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*
        sensores = new Sensores(10, 1234);
        centrosDatos = new CentrosDatos(1, 1234);
        estado = new EstadoHC(sensores, centrosDatos);
        estado.generarEstadoInicial();
        
        
        debug();
        Problem problem = new Problem(  estado, 
                                        new SuccessorFunctionHC(), 
                                        new GoalTestHC(),
                                        new HeuristicFunctionHC());
        
        HillClimbingSearch search = new HillClimbingSearch();
        try {
            SearchAgent agent = new SearchAgent(problem, search);  
            
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }    
}
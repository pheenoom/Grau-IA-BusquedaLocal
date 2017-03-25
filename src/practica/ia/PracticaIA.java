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
        
        for (Integer s : estado.getRedSensores().get(indice)) {
            System.out.println("Sensor " + (s + 1) + ": ");
            debugPrintRed_i(s,tab);
        }
    }
    
    public static void debugPrintRed() {
        System.out.println();
        System.out.println("Informacion de la red: ");
        
        for (int i = 0; i < EstadoHC.NUM_CENTROS; ++i) {
            System.out.println("##############################################");
            System.out.println("Centro " + (i + 1) + ", tiene las siguientes conexiones: ");
            for (Integer s : estado.getHijosCentro(i)) {
                System.out.println("\tSensor " + (s + 1) + ": ");
                debugPrintRed_i(s, 2);
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
                     + (nodoDestinoSensor[i] + 1));
        }
    }
    
    
    public static void debug() {
        System.out.println("\t\t########################## Debug ##########################\n");
        debugPrintMatrizDistancias();
        debugPrintMatrizSensorACentro();
        debugPrintSensores();
        debugPrintCentros();
        debugPrintSensorDestino();
        System.out.println("\n\t\t########################## Fi Debug ##########################");
    }
    /*
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
    */
    
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
    
    public static void debugPrintGrafInfo() {
        System.out.println("Data in: ");
        for (int i = 0; i < estado.getSensorDataIn().length; ++i) {            
            System.out.println("S" + (i+1) + " --> " + estado.getSensorDataIn()[i]);
        }
        
        System.out.println("Data out: ");
        for (int i = 0; i < estado.getSensorDataIn().length; ++i) {            
            System.out.println("S" + (i+1) + " --> " + estado.getSensorDataOut()[i]);
        }
        
        System.out.println("Data Loss: ");
        for (int i = 0; i < estado.getSensorDataIn().length; ++i) {            
            System.out.println("S" + (i+1) + " --> " + estado.getSensorDataLoss()[i]);
        }
        
        System.out.println("Coste SENSOR: ");
        for (int i = 0; i < estado.getSensorDataIn().length; ++i) {            
            System.out.println("S" + (i+1) + " --> " + estado.getSensorCoste()[i]);
        }
                
        System.out.println("Coste inicial: " + calculaCoste(estado));
    }
    
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        
        sensores = new Sensores(100, 1234);
        /*
        sensores.add(new Sensor(1, 1, 1));
        sensores.add(new Sensor(2, 1, 2));
        sensores.add(new Sensor(5, 1, 3));
          */      
        centrosDatos = new CentrosDatos(4, 1234);
        //centrosDatos.add(new Centro(1, 0));
        
        estado = new EstadoHC(sensores, centrosDatos);
        estado.generarEstadoInicial();
        
        System.out.println("Coste INICIAL: " + calculaCoste(estado));
        Problem problem = new Problem(  estado, 
                                        new SuccessorFunctionHC(), 
                                        new GoalTestHC(),
                                        new HeuristicFunctionHC());
    
        HillClimbingSearch search = new HillClimbingSearch();
        try {
            SearchAgent agent = new SearchAgent(problem, search);  
            
            //printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            EstadoHC estadoFinal =(EstadoHC) search.getGoalState();
            System.out.println("################  Tiempo total en milisegundos: " + ((System.nanoTime() - startTime)/1e6) + " ################  ");
            
            System.out.println("Coste FINAL: " + calculaCoste(estadoFinal));
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }

    }    

    private static double calculaCoste(EstadoHC estado) {
            double coste = 0.0;
            for (int c = 0; c < EstadoHC.NUM_CENTROS; c++) {
                for (Integer s : estado.getHijosCentro(c)) {
                    coste += estado.getSensorCoste()[s];
                    double aux = Math.pow(estado.getDistanciaSensorACentro(s, c),2.0) 
                            * estado.getSensorDataOut()[s];
                    coste += aux;
                }
            }
            
            return coste;
    }
}
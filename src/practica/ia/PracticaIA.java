/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.ia;

import IA.Red.CentrosDatos;
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
    public static final int NUM_SENSORES    = 100;
    public static final int NUM_CENTROS     = 4;
    
    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }
    
    public static void main(String[] args) {        
        try {
            int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
            int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
            for (int i = 0; i < 10; ++i) {
                System.out.println("Semilla Sensor: " + semillasSensor[i]);
                System.out.println("Semilla Centro: " + semillasCentros[i]);
                
                long tiempoInicial = System.nanoTime();

                Sensores sensores = new Sensores(NUM_SENSORES, semillasSensor[i]);
                CentrosDatos centrosDatos = new CentrosDatos(NUM_CENTROS, semillasCentros[i]);

                EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                estado.generarEstadoInicialGreedy();

                System.out.println("Coste Inicial: " + calculaCoste(estado));
                System.out.println("Perdidas Inicial: " + calculaPerdidas(estado));

                Problem problem = new Problem(  estado, 
                                                new SuccessorFunctionHC(), 
                                                new GoalTestHC(),
                                                new HeuristicFunctionHC());

                HillClimbingSearch search = new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(problem, search);  

                printInstrumentation(agent.getInstrumentation());
                System.out.println("Tiempo total en milisegundos: " + ((System.nanoTime() - tiempoInicial)/1e6));

                EstadoHC estadoFinal =(EstadoHC) search.getGoalState();            
                System.out.println("Coste FINAL: " + calculaCoste(estadoFinal));
                System.out.println("Perdidas FINAL: " + calculaPerdidas(estadoFinal));
                System.out.println("======================================================");
                
            }            
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static double calculaCoste(EstadoHC estado) {
            double coste = 0.0;
            for (int c = 0; c < EstadoHC.NUM_CENTROS; c++) {
                for (Integer s : estado.getHijosCentro(c)) {
                    coste += estado.getSensorCoste(s);
                    double aux = estado.getDistanciaSensorACentro(s, c) 
                            * estado.getSensorDataOut(s);
                    coste += aux;
                }
            }
            
            return coste;
    }

    private static double calculaPerdidas(EstadoHC estadoFinal) {
        double perdidas = 0.0;
        for (int c = 0; c < EstadoHC.NUM_CENTROS; c++) {
            double dataIn = 0.0;
            for (Integer s : estadoFinal.getHijosCentro(c)) {
                perdidas += estadoFinal.getSensorDataLoss(s);
                dataIn += estadoFinal.getSensorDataOut(s);
            }
            if(dataIn > 150)
            {
                 perdidas += dataIn-150;
            }
            
            
        }
        
        return perdidas;
    }
}

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
import aima.search.informed.SimulatedAnnealingSearch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
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
            System.out.println("Nodos: " + property);
        }

    }
    
    private static void printActions(List actions) {
        System.out.println("Camino hacia la solucion: ");
        for (int i = 0; i < actions.size(); i++) {
            String action = actions.get(i).toString();
            System.out.println(action);
        }

        //System.out.println((String)actions.get(actions.size()-1));
    }
    
   
        // Cuantos pasos voy hacer, cuanto mas grande es el numeror mejor es la solucion
    // K = cuanta velocidad tengo que empezar a saltar menos. K mayor es mas estricto es, mas rapido cae la aleatoridad.
    // lambda = propabilidad de cojer un estado. lambda mas pequeño , la probabilidad de cojer uno malo es menor.
    // 
    
    // Experimento 3
    public static double experimento3(int steps, int stiter, int k, double lamb, int semillaSensor, int semillaCentro) throws Exception {
        Sensores sensores = new Sensores(100, semillaSensor);
        CentrosDatos centrosDatos = new CentrosDatos(4, semillaCentro);

        EstadoHC estado = new EstadoHC(sensores, centrosDatos);
        estado.generarEstadoInicialGreedy();

        //System.out.println("Coste Inicial: " + calculaCoste(estado));
        //System.out.println("Perdida Inicial: " + calculaPerdidas(estado));

        Problem problem = new Problem(  estado, 
                                        new SuccessorFunctionSA(), 
                                        new GoalTestHC(),
                                        new HeuristicFunctionHC());

        SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);
        SearchAgent agent = new SearchAgent(problem, search);  

        

        EstadoHC estadoFinal =(EstadoHC) search.getGoalState();  
        return calculaCoste(estadoFinal);
        //System.out.println("Coste Final: " + calculaCoste(estadoFinal));
        //System.out.println("Perdida Final: " + calculaPerdidas(estadoFinal));
        //System.out.println();
        //printActions(agent.getActions());
    }
    
    public static void main(String[] args) {        
        try {
            
            int steps = 10000;
            int stiter = 100;
            LinkedList<Integer> kas = new LinkedList<>();
            kas.add(1);
            kas.add(2);
            kas.add(3);
            kas.add(5);
            kas.add(8);
            kas.add(10);
            kas.add(15);
            kas.add(20);
            kas.add(50);
            kas.add(100);
            
            LinkedList<Double> lambdas = new LinkedList<>();
            lambdas.add(0.1);
            lambdas.add(0.01);
            lambdas.add(0.001);
            lambdas.add(0.0001);
            lambdas.add(0.00001);
            
            for(int k : kas) {
                for (double lamb : lambdas) {
                    int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
                    int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
                    
                    String filename = steps+"steps_" + k + "k_" + lamb + "lamb_";
                    PrintWriter writer = new PrintWriter(filename);
                    System.out.println(filename);
                    
                    for (int i = 0; i < 10; ++i) {
                        //String filename = steps+"steps_" + k + "k_" + lamb + "lamb_" + semillasSensor[i] +  "semillaSensor_" + semillasCentros[i] + "semillasCentro";
                        
                        for (int j = 0; j < 20; ++j) {
                            double cost = experimento3(steps, stiter, k, lamb, semillasSensor[i], semillasCentros[i]);
                            writer.write(String.valueOf(cost)+"\n");
                        }
                    }
                    
                    writer.close();
                }
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

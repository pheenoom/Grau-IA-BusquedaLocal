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
            System.out.print(";" + property);
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
    
    // Experimento 1
    /*
    public static void main(String[] args) {        
        try {
            int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
            int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
            System.out.println("Generador de estados greedy");
            for (int i = 0; i < 10; ++i) {
                    long tiempoInicial = System.nanoTime();

                    Sensores sensores = new Sensores(100, semillasSensor[i]);
                    CentrosDatos centrosDatos = new CentrosDatos(4, semillasCentros[i]);

                    EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                    estado.generarEstadoInicialGreedy();

                    System.out.print(calculaCoste(estado));
                    System.out.print(";" + calculaPerdidas(estado));

                    Problem problem = new Problem(  estado, 
                                                    new SuccessorFunctionHC(), 
                                                    new GoalTestHC(),
                                                    new HeuristicFunctionHC());

                    HillClimbingSearch search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);  

                    printInstrumentation(agent.getInstrumentation());
                    System.out.print(";" + ((System.nanoTime() - tiempoInicial)/1e6));

                    EstadoHC estadoFinal = (EstadoHC) search.getGoalState();            
                    System.out.print(";" + calculaCoste(estadoFinal));
                    System.out.print(";" + calculaPerdidas(estadoFinal));
                    System.out.println();
            }         
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    // Experimento 2
    /*
    public static void main(String[] args) {        
        try {
            int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
            int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
            
            // Generador de estado Greedy
            System.out.println("Generador de estados greedy");
            System.out.println("Tiempo (ms);Nodos;Coste Inicial;Coste Final");
            for (int i = 0; i < 10; ++i) {
                    long tiempoInicial = System.nanoTime();

                    Sensores sensores = new Sensores(100, semillasSensor[i]);
                    CentrosDatos centrosDatos = new CentrosDatos(4, semillasCentros[i]);

                    EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                    estado.generarEstadoInicialGreedy();

                    double costeInicial = calculaCoste(estado);

                    Problem problem = new Problem(  estado, 
                                                    new SuccessorFunctionHC(), 
                                                    new GoalTestHC(),
                                                    new HeuristicFunctionHC());

                    HillClimbingSearch search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);  

                    System.out.print("" + ((System.nanoTime() - tiempoInicial)/1e6));
                    printInstrumentation(agent.getInstrumentation());
                    System.out.print(";" + costeInicial);
                    EstadoHC estadoFinal = (EstadoHC) search.getGoalState();            
                    System.out.print(";" + calculaCoste(estadoFinal));
                    System.out.println();
            }
            
            System.out.println("Generador de estados greedy con ordenacion ascendiente");
            System.out.println("Tiempo (ms);Nodos;Coste Inicial;Coste Final");
            // Generador de estado Greedy con ordenacion ascendiente
            for (int i = 0; i < 10; ++i) {
                    long tiempoInicial = System.nanoTime();

                    Sensores sensores = new Sensores(100, semillasSensor[i]);
                    CentrosDatos centrosDatos = new CentrosDatos(4, semillasCentros[i]);

                    EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                    estado.generarEstadoInicialOrdenado(true);

                    double costeInicial = calculaCoste(estado);

                    Problem problem = new Problem(  estado, 
                                                    new SuccessorFunctionHC(), 
                                                    new GoalTestHC(),
                                                    new HeuristicFunctionHC());

                    HillClimbingSearch search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);  

                    System.out.print("" + ((System.nanoTime() - tiempoInicial)/1e6));
                    printInstrumentation(agent.getInstrumentation());
                    System.out.print(";" + costeInicial);
                    EstadoHC estadoFinal = (EstadoHC) search.getGoalState();            
                    System.out.print(";" + calculaCoste(estadoFinal));
                    System.out.println();
            }
            
            // Generador de estado Greedy con ordenacion descendiente
            System.out.println("Generador de estados greedy con ordenacion descendiente");
            System.out.println("Tiempo (ms);Nodos;Coste Inicial;Coste Final");
            for (int i = 0; i < 10; ++i) {
                    long tiempoInicial = System.nanoTime();

                    Sensores sensores = new Sensores(100, semillasSensor[i]);
                    CentrosDatos centrosDatos = new CentrosDatos(4, semillasCentros[i]);

                    EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                    estado.generarEstadoInicialOrdenado(false);

                    double costeInicial = calculaCoste(estado);

                    Problem problem = new Problem(  estado, 
                                                    new SuccessorFunctionHC(), 
                                                    new GoalTestHC(),
                                                    new HeuristicFunctionHC());

                    HillClimbingSearch search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);  

                    System.out.print("" + ((System.nanoTime() - tiempoInicial)/1e6));
                    printInstrumentation(agent.getInstrumentation());
                    System.out.print(";" + costeInicial);
                    EstadoHC estadoFinal = (EstadoHC) search.getGoalState();            
                    System.out.print(";" + calculaCoste(estadoFinal));
                    System.out.println();
            }
            
            System.out.println("Generador de estados random");
            System.out.println("Tiempo (ms);Nodos;Coste Inicial;Coste Final");
            for (int i = 0; i < 10; ++i) {
                for (int j = 0; j< 5; ++j) {
                    long tiempoInicial = System.nanoTime();

                    Sensores sensores = new Sensores(NUM_SENSORES, semillasSensor[i]);
                    CentrosDatos centrosDatos = new CentrosDatos(NUM_CENTROS, semillasCentros[i]);

                    EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                    estado.generarEstadoInicialRandom();
                    
                    double costeInicial = calculaCoste(estado);

                    Problem problem = new Problem(  estado, 
                                                    new SuccessorFunctionHC(), 
                                                    new GoalTestHC(),
                                                    new HeuristicFunctionHC());

                    HillClimbingSearch search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);  

                    System.out.print("" + ((System.nanoTime() - tiempoInicial)/1e6));
                    printInstrumentation(agent.getInstrumentation());
                    System.out.print(";" + costeInicial);
                    EstadoHC estadoFinal = (EstadoHC) search.getGoalState();            
                    System.out.print(";" + calculaCoste(estadoFinal));
                    System.out.println();
                }
                System.out.println("==================================================================");              
            }
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
    
        // Cuantos pasos voy hacer, cuanto mas grande es el numeror mejor es la solucion
    // K = cuanta velocidad tengo que empezar a saltar menos. K mayor es mas estricto es, mas rapido cae la aleatoridad.
    // lambda = propabilidad de cojer un estado. lambda mas pequeño , la probabilidad de cojer uno malo es menor.
    // 
    
    // Experimento 3
    /*    
    public static void main(String[] args) {        
        try {
            int steps = 10000;
            int stiter = 10;
            int k = 100;
            double lamb = 0.01;
            
            int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
            int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
            
            System.out.println("K;Lambda;Steps;CosteInicial;PerdidasInicial;Nodos;Tiempo;CosteFinal;PerdidasFinal");
            //for (int i = 0; i < 20; ++i) {
                System.out.print(k + ";" + lamb + ";" + steps + ";");
                long tiempoInicial = System.nanoTime();

                Sensores sensores = new Sensores(100, semillasSensor[0]);
                CentrosDatos centrosDatos = new CentrosDatos(4, semillasCentros[0]);

                EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                estado.generarEstadoInicialGreedy();

                System.out.print(calculaCoste(estado));
                System.out.print(";" + calculaPerdidas(estado));

                Problem problem = new Problem(  estado, 
                                                new SuccessorFunctionSA(), 
                                                new GoalTestHC(),
                                                new HeuristicFunctionHC());

                SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);
                SearchAgent agent = new SearchAgent(problem, search);  
                
                printInstrumentation(agent.getInstrumentation());
                System.out.print(";" + ((System.nanoTime() - tiempoInicial)/1e6));

                EstadoHC estadoFinal =(EstadoHC) search.getGoalState();            
                System.out.print(";" + calculaCoste(estadoFinal));
                System.out.print(";" + calculaPerdidas(estadoFinal));
                System.out.println();
                printActions(agent.getActions());
                steps = steps + 1000;
            //}
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
    
    // Experimento 4    
    /*
    public static void main(String[] args) {        
        try {
            int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
            int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
            int numeroCentros = 4;
            int numeroSensores = 100;
            while(true) {
                long tiempoInicial = System.nanoTime();

                Sensores sensores = new Sensores(numeroSensores, semillasSensor[9]);
                CentrosDatos centrosDatos = new CentrosDatos(numeroCentros, semillasCentros[9]);

                EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                estado.generarEstadoInicialGreedy();

                System.out.print(calculaCoste(estado));
                System.out.print(";" + calculaPerdidas(estado));

                Problem problem = new Problem(  estado, 
                                                new SuccessorFunctionHC(), 
                                                new GoalTestHC(),
                                                new HeuristicFunctionHC());

                HillClimbingSearch search = new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(problem, search);  

                printInstrumentation(agent.getInstrumentation());
                System.out.print(";" + ((System.nanoTime() - tiempoInicial)/1e6));

                EstadoHC estadoFinal =(EstadoHC) search.getGoalState();            
                System.out.print(";" + calculaCoste(estadoFinal));
                System.out.print(";" + calculaPerdidas(estadoFinal));
                System.out.print(";" + numeroCentros);
                System.out.print(";" + numeroSensores);
                System.out.println();
                numeroCentros = numeroCentros + 2;
                numeroSensores = numeroSensores + 50;
            }
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     
    */
    
    // Experimento 5
    /*
    public static void main(String[] args) {        
        try {
            int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
            int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
            for (int i = 0; i < 10; ++i) {
                    long tiempoInicial = System.nanoTime();

                    Sensores sensores = new Sensores(100, semillasSensor[i]);
                    CentrosDatos centrosDatos = new CentrosDatos(4, semillasCentros[i]);

                    EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                    estado.generarEstadoInicialGreedy();

                    System.out.print(calculaCoste(estado));
                    System.out.print(";" + calculaPerdidas(estado));

                    Problem problem = new Problem(  estado, 
                                                    new SuccessorFunctionHC(), 
                                                    new GoalTestHC(),
                                                    new HeuristicFunctionHC());

                    HillClimbingSearch search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);  

                    printInstrumentation(agent.getInstrumentation());
                    System.out.print(";" + ((System.nanoTime() - tiempoInicial)/1e6));

                    EstadoHC estadoFinal = (EstadoHC) search.getGoalState();            
                    System.out.print(";" + calculaCoste(estadoFinal));
                    System.out.print(";" + calculaPerdidas(estadoFinal));
                    for (int c = 0; c < EstadoHC.NUM_CENTROS; ++c) {
                        System.out.print(";" + estadoFinal.getRedCentros().get(estadoFinal.getCentro(c)).size());
                    }
                    System.out.println();
            }    
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
    // Experimento 6    
    /*
    public static void main(String[] args) {        
        try {
            int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
            int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
            for (int i = 0; i < 10; ++i) {
                int numeroCentros = 4;
                while (numeroCentros <= 10) {
                    long tiempoInicial = System.nanoTime();

                    Sensores sensores = new Sensores(100, semillasSensor[i]);
                    CentrosDatos centrosDatos = new CentrosDatos(numeroCentros, semillasCentros[i]);

                    EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                    estado.generarEstadoInicialGreedy();

                    System.out.print(numeroCentros);
                    System.out.print(";" + calculaCoste(estado));
                    System.out.print(";" + calculaPerdidas(estado));

                    Problem problem = new Problem(  estado, 
                                                    new SuccessorFunctionHC(), 
                                                    new GoalTestHC(),
                                                    new HeuristicFunctionHC());

                    HillClimbingSearch search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);  

                    printInstrumentation(agent.getInstrumentation());
                    System.out.print(";" + ((System.nanoTime() - tiempoInicial)/1e6));

                    EstadoHC estadoFinal = (EstadoHC) search.getGoalState();            
                    System.out.print(";" + calculaCoste(estadoFinal));
                    System.out.print(";" + calculaPerdidas(estadoFinal));
                    for (int c = 0; c < EstadoHC.NUM_CENTROS; ++c) {
                        System.out.print(";" + estadoFinal.getRedCentros().get(estadoFinal.getCentro(c)).size());
                    }
                    System.out.println();
                    numeroCentros = numeroCentros + 2;
                }
                System.out.println();
            }    
        } catch (Exception ex) {
            Logger.getLogger(PracticaIA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
    // Experimento 7
    public static void main(String[] args) {        
        try {
            int semillasSensor[] = {1143, 2985, 9847, 8417, 8814, 3954, 2901, 2134, 3911, 2242};
            int semillasCentros[] = {9180, 8855, 8580, 4110, 2608, 9290, 4591, 5956, 7715, 8908};
            for (int i = 0; i < 10; ++i) {
                    long tiempoInicial = System.nanoTime();

                    Sensores sensores = new Sensores(100, semillasSensor[i]);
                    CentrosDatos centrosDatos = new CentrosDatos(2, semillasCentros[i]);

                    EstadoHC estado = new EstadoHC(sensores, centrosDatos);
                    estado.generarEstadoInicialGreedy();
                    
                    System.out.print(calculaCoste(estado));
                    System.out.print(";" + calculaPerdidas(estado));

                    Problem problem = new Problem(  estado, 
                                                    new SuccessorFunctionHC(), 
                                                    new GoalTestHC(),
                                                    new HeuristicFunctionHC());

                    HillClimbingSearch search = new HillClimbingSearch();
                    SearchAgent agent = new SearchAgent(problem, search);  

                    printInstrumentation(agent.getInstrumentation());
                    System.out.print(";" + ((System.nanoTime() - tiempoInicial)/1e6));

                    EstadoHC estadoFinal = (EstadoHC) search.getGoalState();            
                    System.out.print(";" + calculaCoste(estadoFinal));
                    System.out.print(";" + calculaPerdidas(estadoFinal));
                    System.out.println();
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

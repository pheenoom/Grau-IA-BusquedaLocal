package practicaia;

import IA.Red.Centro;
import IA.Red.CentrosDatos;
import IA.Red.Sensor;
import IA.Red.Sensores;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class PracticaIAEstado {
    public static final int MAX_CONEXIONES_SENSORES = 3;
    public static final int MAX_CONEXIONES_CENTROS = 25;
    
    private static int NUM_SENSORES;   
    private static int NUM_CENTROS;
    
    private static double[][] matrizDistanciasEntreSensores;
    private static double[][] matrizDistanciasSensoresACentro;
    private static CentrosDatos centros;
    private static Sensores sensores;
    
    private int[] sensorDestino;
    private HashMap<Integer,HashSet<Integer>> redSensores;
    private HashMap<Integer,HashSet<Integer>> redCentros;
    
    private double calcularDistancia(Sensor a, Sensor b) {
        return Math.sqrt(Math.pow(a.getCoordX() - b.getCoordX(), 2) 
               + Math.pow(a.getCoordY() - b.getCoordY(), 2));
    }
    
    private double calcularDistancia(Sensor s, Centro c) {
        return Math.sqrt(Math.pow(s.getCoordX() - c.getCoordX(), 2) 
               + Math.pow(s.getCoordY() - c.getCoordY(), 2));
    }
    
    private void calcularDistanciasEntreSensores(){
        for(int i = 0; i < NUM_SENSORES; ++i) {
            for(int j = 0; j < NUM_SENSORES; ++j) {
                matrizDistanciasEntreSensores[i][j] = Math.pow(calcularDistancia(sensores.get(i), sensores.get(j)), 2.0);
            }
        }
    }
    
    private void calcularDistanciasSensoresACentro() {
        for(int i = 0; i < NUM_SENSORES; ++i) {
            for(int j = 0; j < NUM_SENSORES; ++j) {
                matrizDistanciasSensoresACentro[i][j] = Math.pow(calcularDistancia(sensores.get(i), centros.get(j)), 2.0);
            }
        }
    }
    
    private void inicializarRed() {
        for (int i = 0; i < NUM_SENSORES; ++i) {
            this.redSensores.put(i, new HashSet<>());
        }
        
        for (int i = 0; i < NUM_CENTROS; ++i) {
            this.redCentros.put(i, new HashSet<>());
        }
    }
    
    public boolean comprovarGrafoAciclico() {
        return false;
    } 
    
    public double calcularDistanciaSubArbolSensor(int indiceSensor) {
        double distancia = 0.0;
        
        for (Integer s : this.redSensores.get(indiceSensor)) {
            distancia = distancia + matrizDistanciasEntreSensores[indiceSensor][s];
            distancia = distancia + calcularDistanciaSubArbolSensor(s);
        }
        
        return distancia;
    }
    
    public double calcularDistanciaCentroIessimo(int indiceCentro) {
        double distancia = 0.0;
        
        for (Integer s : this.redCentros.get(indiceCentro)) {
            distancia = distancia + matrizDistanciasSensoresACentro[s][indiceCentro]; 
            distancia = distancia + calcularDistanciaSubArbolSensor(s);
        }
        
        return distancia;
    }
    
    public PracticaIAEstado(Sensores sensores, CentrosDatos centros) {
        NUM_SENSORES = sensores.size();
        NUM_CENTROS = centros.size();
        
        this.centros = centros;
        this.sensores = sensores;
        this.redSensores = new HashMap<>();
        this.redCentros = new HashMap<>();
        inicializarRed();
        
        this.sensorDestino = new int[NUM_SENSORES];
        Arrays.fill(sensorDestino, -1);        
        
        matrizDistanciasEntreSensores = new double[NUM_SENSORES][NUM_SENSORES];
        calcularDistanciasEntreSensores();
        
        matrizDistanciasSensoresACentro = new double[NUM_SENSORES][NUM_CENTROS];
        calcularDistanciasSensoresACentro();
    }
        
    public void generarEstadoInicial() {
        int indiceSensor = 0;
        int indiceCentro = 0;
        while (indiceSensor < NUM_SENSORES && 
                this.redCentros.get(NUM_CENTROS - 1).size() < MAX_CONEXIONES_CENTROS) {
            
            this.redCentros.get(indiceCentro).add(indiceSensor);
            
            ++indiceSensor;
            indiceCentro = ++indiceCentro % NUM_CENTROS;            
        }
        
        int offset = NUM_CENTROS * MAX_CONEXIONES_CENTROS;
        int j = 1;
        int i = 0;
        
        while (indiceSensor < NUM_SENSORES) {
            if (this.redSensores.get(offset * j).size() >= MAX_CONEXIONES_SENSORES) {
                j = j * MAX_CONEXIONES_SENSORES;
            }
            
            i = (i % (offset * j)) + ((j == 1) ? 0 : offset * (j/3));
            
            this.redSensores.get(i).add(indiceSensor);
            
            ++indiceSensor;
            ++i;
        }
    }
    
    public boolean movimientoValido(int sensorA, int sensorB) {
        if (sensorA == sensorB) return false;
        return true;
    }
    
    public boolean conexionesMaximasSensor(int indiceSensor) {
        return this.redSensores.get(indiceSensor).size() < MAX_CONEXIONES_SENSORES;        
    }
    
    public boolean conexionesMaximasCentro(int indiceCentro) {
        return this.redCentros.get(indiceCentro).size() < MAX_CONEXIONES_CENTROS;
    }
    
    public static int getNUM_SENSORES() {
        return NUM_SENSORES;
    }

    public static int getNUM_CENTROS() {
        return NUM_CENTROS;
    }

    /// Metodos Debug
    
    public void debugPrintMatrizDistancias() {
        System.out.println("Matriz de distancias entre sensores: ");
        for (int i = 0; i < NUM_SENSORES; ++i) {
            System.out.print("\t");
            for (int j = 0; j < NUM_SENSORES; ++j) {
                System.out.format("%.4f ", matrizDistanciasEntreSensores[i][j]);
            }
            System.out.println();
        }
    }
    
    public void debugPrintSensores() {
        System.out.println("Informacion de los sensores: ");
        for (int i = 0; i < NUM_SENSORES; ++i) {
            System.out.println("\tSensor " + i);
            System.out.println("\tCapacidad: " + sensores.get(i).getCapacidad());
            System.out.println("\tPosicion (X,Y): (" + 
                    sensores.get(i).getCoordX() + "," + 
                    sensores.get(i).getCoordY() + ")");
        }
    }
    
    private void debugPrintRed_i(int indice, int tab) {
        for (int i = 0; i < tab; ++i) {
            System.out.print("\t");
        }
        System.out.println("Sensor " + indice + ": ");
        for (Integer s : this.redSensores.get(indice)) {
            debugPrintRed_i(s,++tab);
        }
    }
    
    public void debugPrintRed() {
        System.out.println("Informacion de la red: ");
        for (int i = 0; i < NUM_CENTROS; ++i) {
            System.out.println("##############################################");
            System.out.println("Centro " + i + ", tiene las siguientes conexiones: ");
            for (Integer s : this.redCentros.get(i)) {
                debugPrintRed_i(s, 1);
            }
            System.out.println("##############################################");
        }
    }
}

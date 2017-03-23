package practica.ia;

import IA.Red.Centro;
import IA.Red.CentrosDatos;
import IA.Red.Sensor;
import IA.Red.Sensores;
import java.util.HashMap;
import java.util.HashSet;

public class EstadoHC{
    public static final int MAX_CONEXIONES_SENSORES = 3;
    public static final int MAX_CONEXIONES_CENTROS = 25;
    
    public static int NUM_SENSORES;   
    public static int NUM_CENTROS;
    
    private static double[][] matrizDistanciasEntreSensores;
    private static double[][] matrizDistanciasSensoresACentro;
    private static CentrosDatos centros;
    private static Sensores sensores;
    
    private int[] nodoDestinoSensor;
    private byte[] tipoNodoDestinoSensor;
    private HashMap<Integer,HashSet<Integer>> redSensores;
    private HashMap<Integer,HashSet<Integer>> redCentros;

    ////////////////////////////////////////////////////////////////////////////
    ///                         Metodos privados                             ///
    ////////////////////////////////////////////////////////////////////////////
    
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
                matrizDistanciasEntreSensores[i][j] = calcularDistancia(sensores.get(i), sensores.get(j));
            }
        }
    }
    
    private void calcularDistanciasSensoresACentro() {
        for(int i = 0; i < NUM_SENSORES; ++i) {
            for(int j = 0; j < NUM_CENTROS; ++j) {
                matrizDistanciasSensoresACentro[i][j] = calcularDistancia(sensores.get(i), centros.get(j));
            }
        }
    }    
    
    private void desconectarSensorAEnSesnsorB(int a, int b) {
        this.redSensores.get(b).remove(a);
        this.nodoDestinoSensor[a] = -1;
        this.tipoNodoDestinoSensor[a] = '-';
    }
    
    private void desconectarSensorEnCentro(int s, int c) {
        this.redCentros.get(c).remove(s);
        this.nodoDestinoSensor[s] = -1;
        this.tipoNodoDestinoSensor[s] = '-';
    }
    
    private void conectarSensorAEnSensorB(int a, int b) {
        this.redSensores.get(b).add(a);
        this.nodoDestinoSensor[a] = b;
        this.tipoNodoDestinoSensor[a] = 'S';
    }
    
    private void conectarSensorEnCentro(int s, int c) {
        this.redSensores.get(c).add(s);
        this.nodoDestinoSensor[s] = c;
        this.tipoNodoDestinoSensor[s] = 'C';
    }
    
    private void inicializarRed() {
        for (int i = 0; i < NUM_SENSORES; ++i) {
            this.redSensores.put(i, new HashSet<>());
        }       
        
        for (int i = 0; i < NUM_CENTROS; ++i) {
            this.redCentros.put(i, new HashSet<>());
        }
    }
    
    private boolean esCentro(int indice) {
        return tipoNodoDestinoSensor[indice] == 'C';
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///                            Constructor                               ///
    ////////////////////////////////////////////////////////////////////////////
    
    EstadoHC(EstadoHC estado) {
        this.redSensores = estado.copiaRedSensores();
        this.redCentros = estado.copiaRedCentros();
        this.nodoDestinoSensor = estado.nodoDestinoSensor.clone();
        this.tipoNodoDestinoSensor = estado.tipoNodoDestinoSensor.clone();
    }
    
    public EstadoHC(Sensores sensores, CentrosDatos centros) {
        NUM_SENSORES = sensores.size();
        NUM_CENTROS = centros.size();
        
        this.centros = centros;
        this.sensores = sensores;
        
        this.redSensores = new HashMap<>();
        this.redCentros = new HashMap<>();
        inicializarRed();
        
        this.nodoDestinoSensor = new int[NUM_SENSORES];
        this.tipoNodoDestinoSensor = new byte[NUM_SENSORES];
        
        matrizDistanciasEntreSensores = new double[NUM_SENSORES][NUM_SENSORES];
        calcularDistanciasEntreSensores();
        
        matrizDistanciasSensoresACentro = new double[NUM_SENSORES][NUM_CENTROS];
        calcularDistanciasSensoresACentro();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///                         Metodos publicos                             ///
    ////////////////////////////////////////////////////////////////////////////
   
    public final HashMap<Integer, HashSet<Integer>> copiaRedSensores() {
        HashMap<Integer, HashSet<Integer>> copia = new HashMap<>();
        for (Integer key : this.redSensores.keySet()) {
            copia.put(key, new HashSet<>());
            
            for (Integer value : this.redSensores.get(key)) {
                copia.get(key).add(value);
            }            
        }
        
        return copia;
    }
    
    public final HashMap<Integer, HashSet<Integer>> copiaRedCentros() {
        HashMap<Integer, HashSet<Integer>> copia = new HashMap<>();
        for (Integer key : this.redCentros.keySet()) {
            copia.put(key, new HashSet<>());
            
            for (Integer value : this.redCentros.get(key)) {
                copia.get(key).add(value);
            }            
        }
        
        return copia;
    }
        
    public void generarEstadoInicial() {
        int indiceSensor = 0;
        int indiceCentro = 0;
        while (indiceSensor < NUM_SENSORES && aceptaConexion(NUM_CENTROS - 1)) {            
            this.redCentros.get(indiceCentro).add(indiceSensor);
            this.nodoDestinoSensor[indiceSensor] = indiceCentro;
            this.tipoNodoDestinoSensor[indiceSensor] = 'C';
            
            ++indiceSensor;            
            indiceCentro = (++indiceCentro) % NUM_CENTROS;
        }
        
        int offset = NUM_CENTROS * MAX_CONEXIONES_CENTROS;
        int j = 1;
        int padre = 0;
        
        while (indiceSensor < NUM_SENSORES) {
            if (this.redSensores.get(offset * j).size() >= MAX_CONEXIONES_SENSORES) {
                j = j * MAX_CONEXIONES_SENSORES;
            }
            
            padre = (padre % (offset * j)) + ((j == 1) ? 0 : offset * (j/3));
            
            this.nodoDestinoSensor[indiceSensor] = padre;
            this.tipoNodoDestinoSensor[indiceSensor] = 'S';
            
            this.redSensores.get(padre).add(indiceSensor);
            
            ++indiceSensor;
            ++padre;
        }
    }
    
    public boolean hayCiclos(int origen) {
        int aux = origen;
        
        while(!esCentro(nodoDestinoSensor[aux])){
            aux = nodoDestinoSensor[aux];
            if(aux == origen)
                return true;
        }
        
        return false;
    } 

    public boolean mover(int sensor, int destino, byte tipoDestino) {
        int destinoAnterior = nodoDestinoSensor[sensor];
        byte tipoDestinoAnterior = tipoNodoDestinoSensor[sensor];
               
        nodoDestinoSensor[sensor] = destino;
        tipoNodoDestinoSensor[sensor] = tipoDestino;
        
        if(tipoDestino == 'S'){
            if(!sensorAceptaConexion(sensor) || hayCiclos(destino)) {
                return false;
            }
        }
        else {
            if (!centroAceptaConexion(destino)) {
                return false;
            }
        }
        
        if (tipoDestinoAnterior == 'C') {                    
            desconectarSensorEnCentro(sensor, destinoAnterior);
        }
        else {
            desconectarSensorAEnSesnsorB(sensor, destinoAnterior);
        }
        
        if (tipoDestino == 'C') {
            conectarSensorEnCentro(sensor, destino);
        }
        else {
            conectarSensorAEnSensorB(sensor, destino);                
        }        
        
        return true;
    }
    
    public boolean intercambiar(int sensorA, int sensorB){
        /*
        int destinoA = nodoDestinoSensor[sensorA];
        byte tipoDestinoA = tipoNodoDestinoSensor[sensorA];
        
        this.nodoDestinoSensor[sensorA] = this.nodoDestinoSensor[sensorB];
        this.tipoNodoDestinoSensor[sensorA] = this.tipoNodoDestinoSensor[sensorB];
        this.nodoDestinoSensor[sensorB] = destinoA;
        this.tipoNodoDestinoSensor[sensorB] = tipoDestinoA;
        
        return !hayCiclos(sensorA) && !hayCiclos(sensorB);
*/
        int destinoA = nodoDestinoSensor[sensorA];
        byte tipoDestinoA = tipoNodoDestinoSensor[sensorA];
        int destinoB = nodoDestinoSensor[sensorB];
        byte tipoDestinoB = tipoNodoDestinoSensor[sensorB];
        return  mover(sensorA, destinoB, tipoDestinoB) 
                && mover(sensorB, destinoA, tipoDestinoA);
    }
    
    public boolean movimientoValido(int sensor) {
        int destino = nodoDestinoSensor[sensor];
        return aceptaConexion(destino) && !hayCiclos(destino);
    }
    
    public boolean aceptaConexion(int destino) {
        if(esCentro(destino)) {
            return centroAceptaConexion(destino);
        }
        else {
            return sensorAceptaConexion(destino);
        }
    }
    
    public boolean sensorAceptaConexion(int indiceSensor) {
        return this.redSensores.get(indiceSensor).size() < MAX_CONEXIONES_SENSORES;        
    }
    
    public boolean centroAceptaConexion(int indiceCentro) {
        return this.redCentros.get(indiceCentro).size() < MAX_CONEXIONES_CENTROS;
    }    
        
    HashMap<Integer,HashSet<Integer>> getRedSensores(){
        return this.redSensores;
    }
    
    HashMap<Integer,HashSet<Integer>> getRedCentros(){
        return this.redCentros;
    }
    
    public double getDistanciaEntreSensores(int a, int b) {
        return matrizDistanciasEntreSensores[a][b];        
    }
    
    public double getDistanciaSensorACentro(int s, int c) {
        return matrizDistanciasSensoresACentro[s][c];
    }
    
    public byte[] getTipoNodoDestinoSensor() {
        return this.tipoNodoDestinoSensor;
    }
    
    public int[] getNodoDestinoSensor() {
        return this.nodoDestinoSensor;
    }
    
    public Sensores getSensores() {
        return this.sensores;
    }
    
    public CentrosDatos getCentros() {
        return this.centros;
    }
}

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
    
    private int[] destinos;
    private byte[] tipos;
    private HashMap<Integer,HashSet<Integer>> hijosSensores;
    private HashMap<Integer,HashSet<Integer>> hijosCentros;

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
        this.hijosSensores.get(b).remove(a);
        this.destinos[a] = -1;
        this.tipos[a] = '-';
    }
    
    private void desconectarSensorEnCentro(int s, int c) {
        this.hijosCentros.get(c).remove(s);
        this.destinos[s] = -1;
        this.tipos[s] = '-';
    }
    
    private void conectarSensorAEnSensorB(int a, int b) {
        this.hijosSensores.get(b).add(a);
        this.destinos[a] = b;
        this.tipos[a] = 'S';
    }
    
    private void conectarSensorEnCentro(int s, int c) {
        this.hijosSensores.get(c).add(s);
        this.destinos[s] = c;
        this.tipos[s] = 'C';
    }
    
    private void inicializarRed() {
        for (int i = 0; i < NUM_SENSORES; ++i) {
            this.hijosSensores.put(i, new HashSet<>());
        }       
        
        for (int i = 0; i < NUM_CENTROS; ++i) {
            this.hijosCentros.put(i, new HashSet<>());
        }
    }
    
    private boolean esCentro(int indice) {
        return tipos[indice] == 'C';
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///                            Constructor                               ///
    ////////////////////////////////////////////////////////////////////////////
    
    EstadoHC(EstadoHC estado) {
        this.hijosSensores = estado.copiaRedSensores();
        this.hijosCentros = estado.copiaRedCentros();
        this.destinos = estado.destinos.clone();
        this.tipos = estado.tipos.clone();
    }
    
    public EstadoHC(Sensores sensores, CentrosDatos centros) {
        NUM_SENSORES = sensores.size();
        NUM_CENTROS = centros.size();
        
        this.centros = centros;
        this.sensores = sensores;
        
        this.hijosSensores = new HashMap<>();
        this.hijosCentros = new HashMap<>();
        inicializarRed();
        
        this.destinos = new int[NUM_SENSORES];
        this.tipos = new byte[NUM_SENSORES];
        
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
        for (Integer key : this.hijosSensores.keySet()) {
            copia.put(key, new HashSet<>());
            
            for (Integer value : this.hijosSensores.get(key)) {
                copia.get(key).add(value);
            }            
        }
        
        return copia;
    }
    
    public final HashMap<Integer, HashSet<Integer>> copiaRedCentros() {
        HashMap<Integer, HashSet<Integer>> copia = new HashMap<>();
        for (Integer key : this.hijosCentros.keySet()) {
            copia.put(key, new HashSet<>());
            
            for (Integer value : this.hijosCentros.get(key)) {
                copia.get(key).add(value);
            }            
        }
        
        return copia;
    }
        
    public void generarEstadoInicial() {
        int indiceSensor = 0;
        int indiceCentro = 0;
        while (indiceSensor < NUM_SENSORES && aceptaConexion(NUM_CENTROS - 1)) {            
            this.hijosCentros.get(indiceCentro).add(indiceSensor);
            this.destinos[indiceSensor] = indiceCentro;
            this.tipos[indiceSensor] = 'C';
            
            ++indiceSensor;            
            indiceCentro = (++indiceCentro) % NUM_CENTROS;
        }
        
        int offset = NUM_CENTROS * MAX_CONEXIONES_CENTROS;
        int j = 1;
        int padre = 0;
        
        while (indiceSensor < NUM_SENSORES) {
            if (this.hijosSensores.get(offset * j).size() >= MAX_CONEXIONES_SENSORES) {
                j = j * MAX_CONEXIONES_SENSORES;
            }
            
            padre = (padre % (offset * j)) + ((j == 1) ? 0 : offset * (j/3));
            
            this.destinos[indiceSensor] = padre;
            this.tipos[indiceSensor] = 'S';
            
            this.hijosSensores.get(padre).add(indiceSensor);
            
            ++indiceSensor;
            ++padre;
        }
    }
    
    public boolean hayCiclos(int origen) {
        int aux = origen;
        
        while(!esCentro(destinos[aux])){
            aux = destinos[aux];
            if(aux == origen)
                return true;
        }
        
        return false;
    } 

    public boolean mover(int sensor, int destino, byte tipoDestino) {
        int destinoAnterior = destinos[sensor];
        byte tipoDestinoAnterior = tipos[sensor];
               
        destinos[sensor] = destino;
        tipos[sensor] = tipoDestino;
        
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
        int destinoA = destinos[sensorA];
        byte tipoDestinoA = tipos[sensorA];
        int destinoB = destinos[sensorB];
        byte tipoDestinoB = tipos[sensorB];
        return  mover(sensorA, destinoB, tipoDestinoB) 
                && mover(sensorB, destinoA, tipoDestinoA);
    }
    
    public boolean movimientoValido(int sensor) {
        int destino = destinos[sensor];
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
        return this.hijosSensores.get(indiceSensor).size() < MAX_CONEXIONES_SENSORES;        
    }
    
    public boolean centroAceptaConexion(int indiceCentro) {
        return this.hijosCentros.get(indiceCentro).size() < MAX_CONEXIONES_CENTROS;
    }    
        
    HashMap<Integer,HashSet<Integer>> getRedSensores(){
        return this.hijosSensores;
    }
    
    HashMap<Integer,HashSet<Integer>> getRedCentros(){
        return this.hijosCentros;
    }
    
    public double getDistanciaEntreSensores(int a, int b) {
        return matrizDistanciasEntreSensores[a][b];        
    }
    
    public double getDistanciaSensorACentro(int s, int c) {
        return matrizDistanciasSensoresACentro[s][c];
    }
    
    public byte[] getTipoNodoDestinoSensor() {
        return this.tipos;
    }
    
    public int[] getNodoDestinoSensor() {
        return this.destinos;
    }
    
    public Sensores getSensores() {
        return this.sensores;
    }
    
    public CentrosDatos getCentros() {
        return this.centros;
    }
}

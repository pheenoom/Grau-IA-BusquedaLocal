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
    private double[] sensorDataIn;
    private double[] sensorDataOut;
    private double[] sensorDataLoss;
    private double[] sensorCoste;
    private double[] centroCoste;
    
    private HashMap<Integer,HashSet<Integer>> hijosSensores;
    //En general, el indice de los centros es su numero + numero de sensores
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
                matrizDistanciasEntreSensores[i][j] = Math.pow(calcularDistancia(sensores.get(i), sensores.get(j)), 2.0);
            }
        }
    }
    
    private void calcularDistanciasSensoresACentro() {
        for(int i = 0; i < NUM_SENSORES; ++i) {
            for(int j = 0; j < NUM_CENTROS; ++j) {
                matrizDistanciasSensoresACentro[i][j] = Math.pow(calcularDistancia(sensores.get(i), centros.get(j)), 2.0);
            }
        }
    }    
        
    private void inicializarRed() {
        for (int i = 0; i < NUM_SENSORES; ++i) {
            this.hijosSensores.put(i, new HashSet<>());
        }       
        
        for (int i = NUM_SENSORES; i < NUM_CENTROS+NUM_SENSORES; ++i) {
            this.hijosCentros.put(i, new HashSet<>());
        }
    }
    
    private void reCalcularDades(int indiceSensor) {
        while (!esCentro(indiceSensor)) {
            double tmpDataIn = 0.0;            
            double tmpCoste = 0.0;
            
            for (Integer s : this.hijosSensores.get(indiceSensor)) {
                tmpDataIn += this.sensorDataOut[s];
                tmpCoste += this.sensorCoste[s];
                
                double d = matrizDistanciasEntreSensores[indiceSensor][s];
                tmpCoste = tmpCoste + d * this.sensorDataOut[s];
            }

            double capacidadSensorDestino = sensores.get(indiceSensor).getCapacidad() * 2.0;
            if (tmpDataIn > capacidadSensorDestino) {
                this.sensorDataOut[indiceSensor] = capacidadSensorDestino * 1.5;
                this.sensorDataLoss[indiceSensor] = tmpDataIn - capacidadSensorDestino;
            }
            else {
                this.sensorDataOut[indiceSensor] = tmpDataIn + capacidadSensorDestino * 0.5;
                this.sensorDataLoss[indiceSensor] = 0.0;
            }
            
            this.sensorDataIn[indiceSensor] = tmpDataIn;
            this.sensorCoste[indiceSensor] = tmpCoste;
            int indiceDestino = this.destinos[indiceSensor];        
    
            
            indiceSensor = indiceDestino;
        }
    }
          
    private void desconectarAenB(int sensor, int destinoAnterior) {
        if (esCentro(destinoAnterior)) {
            desconectarSensorEnCentro(sensor, destinoAnterior);
            this.centroCoste[destinoAnterior - NUM_SENSORES] -= this.sensorCoste[sensor];
        }
        else {            
            desconectarSensorAEnSesnsorB(sensor, destinoAnterior);
            reCalcularDades(destinoAnterior);
        }
    }

    private void conectarAenB(int sensor, int nuevoDestino) {
        if (esCentro(nuevoDestino)) {
            conectarSensorEnCentro(sensor, nuevoDestino);
            this.centroCoste[nuevoDestino - NUM_SENSORES] += this.sensorCoste[sensor];
        }
        else {
            conectarSensorAEnSensorB(sensor, nuevoDestino);
            reCalcularDades(sensor);            
        }        
    }
    
    private boolean esCentro(int indice) {
        return indice >= NUM_SENSORES;
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///                            Constructor                               ///
    ////////////////////////////////////////////////////////////////////////////
    
    EstadoHC(EstadoHC estado) {
        this.hijosSensores = estado.copiaRedSensores();
        this.hijosCentros = estado.copiaRedCentros();
        this.destinos = estado.destinos.clone();
        this.sensorDataIn = estado.sensorDataIn.clone();
        this.sensorDataOut = estado.sensorDataOut.clone();
        this.sensorDataLoss = estado.sensorDataLoss.clone();
        this.sensorCoste = estado.sensorCoste.clone();
        this.centroCoste = estado.centroCoste.clone();
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
        this.sensorDataIn = new double[NUM_SENSORES];
        this.sensorDataOut = new double[NUM_SENSORES];
        this.sensorDataLoss = new double[NUM_SENSORES];
        this.sensorCoste = new double[NUM_SENSORES];
        this.centroCoste = new double[NUM_CENTROS];
        
              
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
        //Los centros estan en el rango [NUM_SENSORES, NUM_SENSORES + NUM_CENTROS)
        int indiceCentro = NUM_SENSORES;
        int indexUltimoCentro = NUM_SENSORES + NUM_CENTROS - 1;
       
        //Mientras que queden sensores y el ultimo centro acepte conexiones        
        while (indiceSensor < NUM_SENSORES && aceptaConexion(indexUltimoCentro)) {
            this.hijosCentros.get(indiceCentro).add(indiceSensor);
            this.destinos[indiceSensor] = indiceCentro;
            this.sensorDataOut[indiceSensor] = sensores.get(indiceSensor).getCapacidad();
            
            ++indiceSensor;
            
            ++indiceCentro;
            if(indiceCentro > indexUltimoCentro)
                indiceCentro = NUM_SENSORES;
        }
        
        //Numero de conexiones a centros totales
        int j = 1;
        int padre = 0;
        int indicePrimerSensor = 0;
        int indiceUltimoSensor = NUM_CENTROS * MAX_CONEXIONES_CENTROS;
        while (indiceSensor < NUM_SENSORES) {
            if (this.hijosSensores.get(indiceUltimoSensor - 1).size() >= MAX_CONEXIONES_SENSORES) {
                indicePrimerSensor = indiceUltimoSensor;
                indiceUltimoSensor = indiceUltimoSensor + indicePrimerSensor * MAX_CONEXIONES_SENSORES;
            }
            
            if (padre >= indiceUltimoSensor) {
                padre = indicePrimerSensor;
            }
            
            this.destinos[indiceSensor] = padre;            
            this.hijosSensores.get(padre).add(indiceSensor);
            this.sensorDataOut[indiceSensor] = sensores.get(indiceSensor).getCapacidad();
            
            ++indiceSensor;
            ++padre;
        }
        
        for (int i = indicePrimerSensor; i < NUM_SENSORES; ++i) {
            if (this.hijosSensores.get(i).isEmpty()) {
                reCalcularDades(i);                
            }
        }
    }
    
    public double[] getSensorDataIn() {
        return this.sensorDataIn;
    }
    
    public double[] getSensorDataOut() {
        return this.sensorDataOut;
    }
    
    public double[] getSensorDataLoss() {
        return this.sensorDataLoss;
    }
    
    public double[] getSensorCoste() {
        return this.sensorCoste;
    }
    
    public double[] getCentroCoste() {
        return this.centroCoste;
    }
    
    //Origen siempre ha de ser un sensor
    public boolean hayCiclos(int origen, int futuroDestino) {
        int aux = futuroDestino;
        
        while(!esCentro(aux)){
            aux = destinos[aux];
            if(aux == origen)
                return true;
        }
        
        return false;
    } 
    
    public void mover(int sensor, int nuevoDestino) {        
        desconectarAenB(sensor, destinos[sensor]);
        conectarAenB(sensor, nuevoDestino);
    }
    
    private void desconectarSensorAEnSesnsorB(int a, int b) {
        this.hijosSensores.get(b).remove(a);
        this.destinos[a] = -1;
    }
    
    private void desconectarSensorEnCentro(int s, int c) {
        this.hijosCentros.get(c).remove(s);
        this.destinos[s] = -1;
    }
    
    private void conectarSensorAEnSensorB(int a, int b) {
        this.hijosSensores.get(b).add(a);
        this.destinos[a] = b;
    }
    
    private void conectarSensorEnCentro(int s, int c) {
        this.hijosCentros.get(c).add(s);
        this.destinos[s] = c;
    }
  
    public void intercambiar(int sensorA, int sensorB){
        int destinoA = destinos[sensorA];
        int destinoB = destinos[sensorB];
        mover(sensorA, destinoB);
        mover(sensorB, destinoA);
    }
    
    //Se ha "movido sensor", por lo tanto hay que comprobar que su nuevo
    //destino acepta conexiones y que no se ha formado un ciclo 
    //(si se ha formado un nuevo ciclo, 
    //necesariamente ha de pasar por 'sensor' y 'destino')
    public boolean movimientoValido(int sensor, int futuroDestino) {
        
        return destinos[sensor] != futuroDestino 
                && sensor != futuroDestino
                && aceptaConexion(futuroDestino) 
                && !hayCiclos(sensor, futuroDestino);
    }
    
    public boolean intercambioValido(int sensorA, int sensorB){
        int destinoA = destinos[sensorA];
        int destinoB = destinos[sensorB];
        return movimientoValido(sensorA,destinoB) 
                && movimientoValido(sensorB, destinoA);
        
    }    
    
    public boolean aceptaConexion(int nodo) {
        if(esCentro(nodo)) {
            return centroAceptaConexion(nodo);
        }
        else {
            return sensorAceptaConexion(nodo);
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
    
    int getCentrosSize(){
        return this.hijosCentros.size();
    }
    
    public HashMap<Integer, HashSet<Integer>> getRedCentros() {
        return this.hijosCentros;
    }
    
    public double getDistanciaEntreSensores(int a, int b) {
        return matrizDistanciasEntreSensores[a][b];        
    }
    
    //Indica el numero de sensor y de centro NO SUS INDICES
    public double getDistanciaSensorACentro(int s, int c) {
        return matrizDistanciasSensoresACentro[s][c];
    }
    
    public byte[] getTipos() {
        byte[] tipos = new byte[NUM_SENSORES+NUM_CENTROS];
        for(int i = 0; i < NUM_SENSORES; i++)
            tipos[i] = (byte)'S';
        
        for(int i = NUM_SENSORES; i < NUM_CENTROS+NUM_SENSORES; i++)
            tipos[i] = (byte)'C';
        
        return tipos;
    }
    
    public int[] getDestinos() {
        return this.destinos;
    }
    
    public Sensores getSensores() {
        return sensores;
    }
    
    public CentrosDatos getCentros() {
        return centros;
    }

    Iterable<Integer> getHijosCentro(int i) {
        return hijosCentros.get(i+NUM_SENSORES);
    }

    int getCentro(int num_centro) {
        return num_centro+NUM_SENSORES;
    }

}

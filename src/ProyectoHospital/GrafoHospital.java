
package ProyectoHospital;

import InfoHospital.DatosHospital;
import InfoUI.RutaInfo;
import java.util.ArrayList;
import java.util.Random;


public class GrafoHospital {
   
    private String[] nombresNodos; //arreglo simple de nombres
    private int[][] distancias;    //matriz de pesos que usa floyd warchall, guarda los peso de la ruta mas corta total
    private int[][] siguientes;    //matriz para reconstruir camino, con los caminos, hecho con nodos
    private int numVertices;
    private final int MAX_V = 44;  //capacidad máxima fija para validar
    private int[][] matrizPesosOriginal; //es el mapa base, guarda los pesos directos entre lugares
    private Random random; //para el mecanismo de hora loca
    public GrafoHospital() {
        nombresNodos = new String[MAX_V];
        distancias = new int[MAX_V][MAX_V];
        siguientes = new int[MAX_V][MAX_V];
        numVertices = 0;
        matrizPesosOriginal = new int [MAX_V][MAX_V];
        
        //añadidio a
        random = new Random();
        // Inicializar matrices
        for (int i = 0; i < MAX_V; i++) {
            for (int j = 0; j < MAX_V; j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                    matrizPesosOriginal[i][j] = 0; //por a
                }
                else {
                    distancias[i][j] = HospitalApp.INF;
                    matrizPesosOriginal[i][j] = HospitalApp.INF; //por a
                }
                
                siguientes[i][j] = -1; 
            }
        }
    }

//busqueda de lugar del hospital
    private int buscarIndice(String nombre) {
        for (int i = 0; i < numVertices; i++) {
            //validacion de mayus. y minus.
            if (nombresNodos[i].equalsIgnoreCase(nombre.trim())) {
                return i;
            }
        }
        return -1; //no se encontro el lugar
    }
//construir el grafo
    public void agregarNodo(String nombre) {
        if (buscarIndice(nombre) == -1) {
            if (numVertices < MAX_V) {
                nombresNodos[numVertices] = nombre;
                numVertices++;
            } else {
                System.out.println("Error: Se alcanzó el límite de nodos (" + MAX_V + ")");
            }
        }
    }

    public void agregarArista(String u, String v, int peso) {
        int i = buscarIndice(u);
        int j = buscarIndice(v);

        if (i != -1 && j != -1) {
            distancias[i][j] = peso;
            distancias[j][i] = peso;
            siguientes[i][j] = j;
            siguientes[j][i] = i;
            matrizPesosOriginal[i][j] = peso; //se guarda en la copia
            matrizPesosOriginal[j][i] = peso; //se guarda en al copia
        } else {
            System.out.println("Advertencia: No se pudo conectar " + u + " con " + v + " (Nombre incorrecto)");
        }
    }

    // Algoritmo Floyd-Warshall
    public void ejecutarFloydWarshall() {
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    
                    if (distancias[i][k] == HospitalApp.INF || distancias[k][j] == HospitalApp.INF) {
                        continue; 
                    }

                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        siguientes[i][j] = siguientes[i][k];
                    }
                }
            }
        }
    }


    public void simularPesosAleatorios(int maxPeso) {
        // resetea al grafo original
        DatosHospital.cargarMapa(this);
        
        String[] nombres = getNombresNodos();


        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                // Si la arista existía
                if (matrizPesosOriginal[i][j] != 0 && matrizPesosOriginal[i][j] != HospitalApp.INF) {
                    int nuevoPeso = random.nextInt(maxPeso) + 1; // aplica un peso aleatorio entre max
                    matrizPesosOriginal[i][j] = nuevoPeso;
                    matrizPesosOriginal[j][i] = nuevoPeso;
                }
            }
        }
        
        // 3. Recalcular F-W una sola vez
        resetearMatricesParaFloyd(); 
        ejecutarFloydWarshall();     // calcula
    }
    //cuando llamo a actualizar peso, no se cambia la matriz de distancias sino la matriz de pesos
    //entonces llama a resetar matrices y este metodo borra la matriz distancias vieja y la rremplaza con la
    //matriz con el peso nuevo de la aglomeracion(matriz de adyacencia original)
    //y ahroa si llama a floyd warshall con la nueva matriz con los pesos afectados por el mecanismo de actualizacion
    public void actualizarPeso(String u, String v, double factor) {
        /**
        int i = buscarIndice(u);
        int j = buscarIndice(v);

        if (i == -1 || j == -1) {
            System.out.println("Error: Lugar no encontrado.");
            return;
        }

        //verificacion de q si hay conexion entre los lugares
        if (distancias[i][j] == HospitalApp.INF && factor < 100) {
             System.out.println("No hay conexión directa física entre estos puntos.");
             return;
        }

        //se cambia el peso
        int nuevoPeso = (int) (distancias[i][j] * factor);
        if (nuevoPeso <= 0){
            nuevoPeso = 1;
        }

        distancias[i][j] = nuevoPeso;
        distancias[j][i] = nuevoPeso;
        
        System.out.println(">> Peso actualizado: " + u + " <-> " + v + " = " + nuevoPeso + " seg.");
        System.out.println(">> Recalculando todas las rutas...");
        
        ejecutarFloydWarshall(); //se recalcula con floyd warshallll gracias por tanto perdon por poco
        **/
        //cambio 

        int i = buscarIndice(u);
        int j = buscarIndice(v);

        if (i == -1 || j == -1) {
            System.out.println("Error: Lugar no encontrado");
            return;
        }
        
        if (i == j) {
             System.out.println("El origen y el destino son el mismo");
             return;
        }

        // 1. revisar si hay un camino (con la matriz de distancias de floyd)
        if (distancias[i][j] == HospitalApp.INF) {
            // si algo, solo se aplica a la arista
            System.out.println("No hay ruta completa calculada entre " + u + " y " + v + ".");
            
            // revisamos si de verdad hay una arista directa en el mapa original
            if (matrizPesosOriginal[i][j] == HospitalApp.INF || matrizPesosOriginal[i][j] == 0) {
                 System.out.println("Tampoco hay conexión directa");
                 return;
            }
            
            System.out.println("Aplicando mecanismo " + factor + " solo a la arista directa: " + u + "" + v);
            
            // calcular el nuevo peso para esa arista directa
            int nuevoPeso;
            if (factor >= HospitalApp.INF) {
                nuevoPeso = HospitalApp.INF;
            } else {
                nuevoPeso = (int) (matrizPesosOriginal[i][j] * factor);
            }

            if (nuevoPeso <= 0 && nuevoPeso != HospitalApp.INF){
                 nuevoPeso = 1; // el peso minimo es 1
            }

            // modificar la matriz de pesos original (la de adyacencia)
            matrizPesosOriginal[i][j] = nuevoPeso;
            matrizPesosOriginal[j][i] = nuevoPeso;
            
            System.out.println("  -> Arista " + u + " <-> " + v + " actualizada a " + nuevoPeso);

        } else {
            // si hay ruta, le aplicamos eso a tdos las asristas de esa ruta
            System.out.println("Aplicando mecanismo" + factor + " al camino: " + u + "  " + v);
            ArrayList<Integer> rutaIndices = new ArrayList<>();
            int actual = i;
            while (actual != j) {
                rutaIndices.add(actual);
                int salto = siguientes[actual][j]; // sacamos el proximo salto
                if (salto == -1) {
                     System.out.println("Error al reconstruir la ruta");
                     return;
                }
                actual = salto;
            }
            rutaIndices.add(j); // añade el destino al final
            // recorre lo que se hizo y modificar la matriz original
            for (int k = 0; k < rutaIndices.size() - 1; k++) {
                int nodoa = rutaIndices.get(k);
                int nodob = rutaIndices.get(k + 1);
                String nombreA = nombresNodos[nodoa];
                String nombreB = nombresNodos[nodob];

                // camino hecho de aristas
                if (matrizPesosOriginal[nodoa][nodob] == HospitalApp.INF || matrizPesosOriginal[nodoa][nodob] == 0) {
                     System.out.println(" El segmento " + nombreA + " y" + nombreB + " no existe en la matriz original");
                     continue;
                }
                
                // nuevo peso
                int nuevoPeso;
                if (factor >= HospitalApp.INF) {
                    nuevoPeso = HospitalApp.INF;
          } else {
                    //saca el peso del mapa original y lo multiplica
                    nuevoPeso = (int) (matrizPesosOriginal[nodoa][nodob] * factor);
                }
                
                if (nuevoPeso <= 0 && nuevoPeso != HospitalApp.INF){
                  nuevoPeso = 1; //peso minimo
             }

                // modificar la matriz de pesos original
                matrizPesosOriginal[nodoa][nodob] = nuevoPeso;
                matrizPesosOriginal[nodob][nodoa] = nuevoPeso; //                 
                System.out.println(" Segmento " + nombreA + " " + nombreB + " actualizado a " + nuevoPeso);
            }
        }
        System.out.println(" Recalculando todas las rutas");
        resetearMatricesParaFloyd();
        ejecutarFloydWarshall();
        System.out.println(" completado");

    
    }
    public String[] getNombresNodos() {
        //devuelve una copia de los nombres de los nodos para la interfaz
        String[] copia = new String[numVertices];
        System.arraycopy(nombresNodos, 0, copia, 0, numVertices);
        return copia;
    }
    public int getNumVertices() {
        return numVertices;
    }
    public int[][] getMatrizAdyacenciaOriginal() { //devuelve la matriz original para la interfaz
        return matrizPesosOriginal;
    }
    public int getINF() {
        return HospitalApp.INF;
    }

    public int[][] getDistancias() {
        return distancias;
    }
    
    //devuelve la ruta mas corta creada para la interfaz
    public RutaInfo obtenerRuta(String origen, String destino) {
        int u = buscarIndice(origen);
        int v = buscarIndice(destino);
        ArrayList<String> nodosRuta = new ArrayList<>();

        if (u == -1 || v == -1) {
            return new RutaInfo(0, nodosRuta, false); // No existe
        }

        if (distancias[u][v] == HospitalApp.INF) {
            return new RutaInfo(0, nodosRuta, false); // No hay camino
        }

        // si existe el camnio, se recalcula la ruta
        int actual = u;
        while (actual != v) {
            nodosRuta.add(nombresNodos[actual]);
            actual = siguientes[actual][v];
            if (actual == -1) {
                // sale en un error
                return new RutaInfo(0, new ArrayList<>(), false);
            }
        }
        nodosRuta.add(nombresNodos[v]); // Añadir el destino

        return new RutaInfo(distancias[u][v], nodosRuta, true);
    }
    
    //se usa para borrar los datos antiguos de la matriz distancias y copia los nuevos calculos hechos en matriz adyacencia originalmatriz pesos con el mecanismo
    private void resetearMatricesParaFloyd() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                distancias[i][j] = matrizPesosOriginal[i][j]; //copia el peso del grafo matriz con la edicion

                // reconstruye la matriz siguientes
                if (i == j) {
                    distancias[i][j] = 0;
                    siguientes[i][j] = -1; 
                } else if (distancias[i][j] != HospitalApp.INF) {
                    siguientes[i][j] = j; // siguiente nodo es el directo
                } else {
                    siguientes[i][j] = -1; // si no hay ruta directa, sale eso
                }
            }
        }
    }
    //toca limpiar el grafo :( para poder recalcular con el mecanismo de actualizacion
    public void limpiar() {
        numVertices = 0;
        // inicializa las matrices
        for (int i = 0; i < MAX_V; i++) {
            for (int j = 0; j < MAX_V; j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                    matrizPesosOriginal[i][j] = 0;
                } else {
                    distancias[i][j] = HospitalApp.INF;
                    matrizPesosOriginal[i][j] = HospitalApp.INF;
                }
                siguientes[i][j] = -1;
            }
        }
    }
    
    // para la terminal, si se usa
    public void imprimirRuta(String origen, String destino) {
        int u = buscarIndice(origen);
        int v = buscarIndice(destino);

        if (u == -1 || v == -1) {
            System.out.println("Error: Uno de los lugares no existe en el mapa.");
            return;
        }

        if (distancias[u][v] == HospitalApp.INF) {
            System.out.println("No existe ruta disponible (camino bloqueado).");
            return;
        }

        System.out.println("Ruta encontrada");
        System.out.println("Tiempo estimado: " + distancias[u][v] + " segundos.");
        System.out.print("Recorrido: " + nombresNodos[u]);

        int actual = u;
        while (actual != v) {
            actual = siguientes[actual][v];
            if (actual == -1) {
                System.out.print(" Error camino");
                break;
            }
            System.out.print("-> " + nombresNodos[actual]);
        }
        System.out.println("\n");
        
        RutaInfo ruta = obtenerRuta(origen, destino);

    if (!ruta.existe) {
        System.out.println("Error: Uno de los lugares no existe o no hay ruta.");
        return;
    }

    System.out.println("Ruta encontrada");
    System.out.println("Tiempo estimado: " + ruta.distanciaTotal + " segundos.");
    System.out.print("Recorrido: ");
    
    for (int i = 0; i < ruta.nodos.size(); i++) {
        System.out.print(ruta.nodos.get(i));
        if (i < ruta.nodos.size() - 1) {
            System.out.print(" -> ");
        }
    }
    System.out.println("\n");
    }
} 


package pruebaproyectofinal;

import java.util.ArrayList; //nuevas importanciones
import java.util.Random;
import java.util.Scanner;

public class HospitalApp {

    //valor q representa no conexion entre vertices
    static int INF = 999999;

    public static void main(String[] args) {
        GrafoHospital hospital = new GrafoHospital();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Cargando mapa del hospital...");
        
        //cargar plano y grafo
        DatosHospital.cargarMapa(hospital); 

        //calcular rutas iniciales
        hospital.ejecutarFloydWarshall();

        int opcion = 0;
        while (opcion != 5) { //menú con el q hice la prueba
            System.out.println("Bienvenido, ingrese una opcion");
            System.out.println("========================================");
            System.out.println("1. Consultar Ruta General (Origen -> Destino)");
            System.out.println("2. ¿A Donde voy? (Desde Recepcion -> Destino)");
            System.out.println("3. Reportar AGLOMERACION (Aumenta tiempo)"); //algunos eventos pa lo q pide la profe
            System.out.println("4. Reportar EMERGENCIA (Disminuye tiempo)");
            System.out.println("5. Salir");
            System.out.print(">> Seleccione opción: ");
            
            try {
                String input = scanner.nextLine();
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {//validacion datos incorrectos
                continue;
            }

            switch (opcion) {
                case 1: //ruta cualquiera
                    System.out.print("Ingrese Origen: ");
                    String origen = scanner.nextLine();
                    System.out.print("Ingrese Destino: ");
                    String destino = scanner.nextLine();
                    hospital.imprimirRuta(origen, destino);
                    break;

                case 2: //desde ingreso
                    System.out.println("Usted esta aqui: Recepcion");
                    System.out.print("Ingrese Destino (Ej. Laboratorio): ");
                    String destinoR = scanner.nextLine();
                    //se parte desde info.
                    hospital.imprimirRuta("Informacion", destinoR);
                    break;

                case 3: // Aglomeracion
                    System.out.println("[REPORTAR OBSTACULO]");
                    System.out.print("Lugar A: ");
                    String obsA = scanner.nextLine();
                    System.out.print("Lugar B: ");
                    String obsB = scanner.nextLine();
                    hospital.actualizarPeso(obsA, obsB, 2.0); 
                    break;

                case 4: // Emergencia
                    System.out.println("[CODIGO AZUL / PRIORIDAD]");
                    System.out.print("Lugar A: ");
                    String prioA = scanner.nextLine();
                    System.out.print("Lugar B: ");
                    String prioB = scanner.nextLine();
                    hospital.actualizarPeso(prioA, prioB, 0.5);
                    break;
                
                case 5:
                    System.out.println("Cerrando sistema...");
                    break;
                    
                default:
                    System.out.println("Opcion no valida.");
            }
        }
        scanner.close();
    }
}

//plano y grafo
class DatosHospital {
    public static void cargarMapa(GrafoHospital g) {
        //lugares
        g.limpiar();
        String[] lugares = {
            "Entrada Principal", "Pasillo 1", "Informacion", "Farmacia", "Auditorio", 
            "Sala de Espera",
            "Pasillo 3", "Consultorio 12", "Pasillo 2", "Pasillo 4",
            "Imagenologia", "Urgencias", "SubPasillo 1", 
            "Emergencias Ped.", "Sala Emergencias", "Enfermeria 1",
            "SubPasillo 3", "Salas Descanso", "Almacen", "Anatomia Patologica", "Consultorio 11",
            "Vestidor H", "Vestidor M", "Lavanderia", 
            "Estacion Servicio Cocina", "Cocina", "Comedor",
            "Banos", "Enfermeria 2", "PYP", 
            "SubPasillo 4", "Consultorio 5", "Consultorio 4", 
            "Consultorio 3", "Consultorio 2", "Consultorio 1",
            "Laboratorio", "Consulta Externa",
            "Trabajo Social", "Consultorio 8", "SubPasillo 2",
            "Consultorio 7", "Consultorio 9", "Consultorio 10"
        };

        for (String lugar : lugares) {
            g.agregarNodo(lugar);
        }

        //aristas y pesos
        //Formato: origen, destino, peso
        
        g.agregarArista("Entrada Principal", "Pasillo 1", 10);
        g.agregarArista("Pasillo 1", "Informacion", 15);
        g.agregarArista("Pasillo 1", "Farmacia", 8);
        g.agregarArista("Pasillo 1", "Auditorio", 8);
        g.agregarArista("Pasillo 1", "Pasillo 2", 21);
        g.agregarArista("Pasillo 1", "Pasillo 3", 21);
        
        g.agregarArista("Pasillo 3", "Consultorio 12", 7);
        g.agregarArista("Pasillo 3", "Sala de Espera", 9);
        g.agregarArista("Sala de Espera", "Informacion", 5);
        g.agregarArista("Pasillo 3", "SubPasillo 2", 9);
        g.agregarArista("Pasillo 3", "Pasillo 4", 10);
        
        g.agregarArista("Pasillo 2", "Imagenologia", 10);
        g.agregarArista("Pasillo 2", "Consultorio 11", 12);
        g.agregarArista("Pasillo 2", "Anatomia Patologica", 12);
        g.agregarArista("Pasillo 2", "SubPasillo 1", 6);
        g.agregarArista("Pasillo 2", "SubPasillo 3", 10);
        
        g.agregarArista("SubPasillo 1", "Emergencias Ped.", 4);
        g.agregarArista("SubPasillo 1", "Sala Emergencias", 6);
        g.agregarArista("SubPasillo 1", "Enfermeria 1", 9);
        g.agregarArista("Sala Emergencias", "Enfermeria 1", 2);
        g.agregarArista("SubPasillo 1", "Urgencias", 8);
        g.agregarArista("Urgencias", "Imagenologia", 3);

        g.agregarArista("SubPasillo 3", "Almacen", 7);
        g.agregarArista("SubPasillo 3", "Salas Descanso", 8);
        g.agregarArista("SubPasillo 3", "Vestidor M", 8);
        g.agregarArista("SubPasillo 3", "Vestidor H", 9);
        g.agregarArista("Vestidor H", "Lavanderia", 3);
        g.agregarArista("Vestidor M", "Lavanderia", 3);
        g.agregarArista("SubPasillo 3", "Lavanderia", 10);
        g.agregarArista("SubPasillo 3", "Estacion Servicio Cocina", 9);
        g.agregarArista("Estacion Servicio Cocina", "Cocina", 4);
        g.agregarArista("Cocina", "Comedor", 3);
        g.agregarArista("SubPasillo 3", "Comedor", 8);
        

        g.agregarArista("Pasillo 4", "SubPasillo 3", 6);
        g.agregarArista("Pasillo 4", "Banos", 5);
        g.agregarArista("Pasillo 4", "SubPasillo 4", 6);
        g.agregarArista("Pasillo 4", "Comedor", 5);
        g.agregarArista("Pasillo 4", "PYP", 5);
        g.agregarArista("PYP", "Enfermeria 2", 3);
        
        g.agregarArista("Enfermeria 2", "SubPasillo 4", 4);
        
        g.agregarArista("Pasillo 4", "Consulta Externa", 9);
        g.agregarArista("Pasillo 4", "Trabajo Social", 7);
        g.agregarArista("Trabajo Social", "Consultorio 8", 3);
        g.agregarArista("Consultorio 8", "SubPasillo 2", 5);
        
        
        g.agregarArista("SubPasillo 4", "Consultorio 5", 8);
        g.agregarArista("SubPasillo 4", "Consultorio 4", 7);
        g.agregarArista("SubPasillo 4", "Consultorio 3", 7);
        g.agregarArista("SubPasillo 4", "Consultorio 2", 7);
        g.agregarArista("SubPasillo 4", "Consultorio 1", 8);
        g.agregarArista("Consultorio 1", "Consultorio 2", 2);
        g.agregarArista("Consultorio 2", "Consultorio 3", 2);
        g.agregarArista("Consultorio 3", "Consultorio 4", 2);
        g.agregarArista("Consultorio 4", "Consultorio 5", 2);
        g.agregarArista("SubPasillo 4", "Cocina", 6);
        g.agregarArista("SubPasillo 4", "Laboratorio", 6);
        g.agregarArista("Laboratorio", "Consulta Externa", 4);
        g.agregarArista("Consulta Externa", "Consultorio 8", 7);

        
        
        g.agregarArista("SubPasillo 2", "Consultorio 7", 4);
        g.agregarArista("SubPasillo 2", "Consultorio 9", 4);
        g.agregarArista("SubPasillo 2", "Consultorio 10", 5);


        System.out.println(">> Datos cargados: " + lugares.length + " lugares conectados.");
    }
}

class GrafoHospital {
    private String[] nombresNodos; //arreglo simple de nombres
    private int[][] distancias;    //matriz de pesos
    private int[][] siguientes;    //matriz para reconstruir camino
    private int numVertices;
    private final int MAX_V = 45;  //capacidad máxima fija para validar
    private int[][] matrizAdyacenciaOriginal; //por a
    private Random random; //por a
    public GrafoHospital() {
        nombresNodos = new String[MAX_V];
        distancias = new int[MAX_V][MAX_V];
        siguientes = new int[MAX_V][MAX_V];
        numVertices = 0;
        matrizAdyacenciaOriginal = new int [MAX_V][MAX_V];
        
        //añadidio a
        random = new Random();
        // Inicializar matrices
        for (int i = 0; i < MAX_V; i++) {
            for (int j = 0; j < MAX_V; j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                    matrizAdyacenciaOriginal[i][j] = 0; //por a
                }
                else {
                    distancias[i][j] = HospitalApp.INF;
                    matrizAdyacenciaOriginal[i][j] = HospitalApp.INF; //por a
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
            matrizAdyacenciaOriginal[i][j] = peso; //por a
            matrizAdyacenciaOriginal[j][i] = peso; //por a
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
    //nuevo metodo de actualizacion
    public ArrayList<String> simularBloqueoSalas(int cantidad) {
        // 1. Resetear el grafo al estado original
        DatosHospital.cargarMapa(this);
        
        ArrayList<String> nodosBloqueados = new ArrayList<>();
        String[] nombres = getNombresNodos(); 
        
        //bloquea salas aleatorias
        for (int i = 0; i < cantidad; i++) {
            String nodoABloquear = nombres[random.nextInt(nombres.length)];
            nodosBloqueados.add(nodoABloquear);
            int idx = buscarIndice(nodoABloquear);
            
            // bloquea todas las conexiones con la sala
            for (int j = 0; j < numVertices; j++) {
                if (idx != j && matrizAdyacenciaOriginal[idx][j] != HospitalApp.INF) {
                    // modifica la matriz del grafi original
                    matrizAdyacenciaOriginal[idx][j] = HospitalApp.INF;
                    matrizAdyacenciaOriginal[j][idx] = HospitalApp.INF;
                }
            }
        }
        
        // recalculo algoritmo
        resetearMatricesParaFloyd(); 
        ejecutarFloydWarshall();     
        
        return nodosBloqueados;
    }
    
    
    public void simularPesosAleatorios(int maxPeso) {
        // resetea al grafo original
        DatosHospital.cargarMapa(this);
        
        String[] nombres = getNombresNodos();


        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                // Si la arista existía
                if (matrizAdyacenciaOriginal[i][j] != 0 && matrizAdyacenciaOriginal[i][j] != HospitalApp.INF) {
                    int nuevoPeso = random.nextInt(maxPeso) + 1; // aplica un peso aleatorio entre max
                    matrizAdyacenciaOriginal[i][j] = nuevoPeso;
                    matrizAdyacenciaOriginal[j][i] = nuevoPeso;
                }
            }
        }
        
        // 3. Recalcular F-W una sola vez
        resetearMatricesParaFloyd(); // Reinicia
        ejecutarFloydWarshall();     // Calcula
    }

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
        //cambio porque cuando hay una emergencia toca recalcular con la matriz de la emergencia y no la anterior
        
        int i = buscarIndice(u);
        int j = buscarIndice(v);

        if (i == -1 || j == -1) {
            System.out.println("Error: Lugar no encontrado.");
            return;
        }

        //usa la maatriz original para ver si hay conexion entres esos puntos
        if (matrizAdyacenciaOriginal[i][j] == HospitalApp.INF) {
            System.out.println("No hay conexión directa física entre estos puntos.");
            return;
        }

        // se calcula el nuevo peso
        int nuevoPeso;
        if (factor >= HospitalApp.INF) { // Para bloqueos (factor = INF)
            nuevoPeso = HospitalApp.INF;
        } else {
            nuevoPeso = (int) (matrizAdyacenciaOriginal[i][j] * factor); //mira la matriz original
        }

        if (nuevoPeso <= 0 && nuevoPeso != HospitalApp.INF){
             nuevoPeso = 1; // El peso mínimo es 1
        }

        // modifica la matriz de adyacencia original
        matrizAdyacenciaOriginal[i][j] = nuevoPeso;
        matrizAdyacenciaOriginal[j][i] = nuevoPeso;
        
        System.out.println(">> Peso (Original) actualizado: " + u + " <-> " + v + " = " + nuevoPeso + " seg.");
        System.out.println(">> Recalculando todas las rutas...");

        // reinicia la matriz para resetear siguientes
        resetearMatricesParaFloyd(); 

        // 3. recalcula floyd warshall
        ejecutarFloydWarshall();
    
    }
    public String[] getNombresNodos() {
        //devuelve una copia de los nombres de los nodos
        String[] copia = new String[numVertices];
        System.arraycopy(nombresNodos, 0, copia, 0, numVertices);
        return copia;
    }
    public int getNumVertices() {
        return numVertices;
    }
    public int[][] getMatrizAdyacenciaOriginal() {
        return matrizAdyacenciaOriginal;
    }
    public int getINF() {
        return HospitalApp.INF;
    }

    public int[][] getDistancias() {
        return distancias;
    }
    
    
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
    
    private void resetearMatricesParaFloyd() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                distancias[i][j] = matrizAdyacenciaOriginal[i][j]; //copia el peso del grafo orginal

                // reconstruye la matriz
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
                    matrizAdyacenciaOriginal[i][j] = 0;
                } else {
                    distancias[i][j] = HospitalApp.INF;
                    matrizAdyacenciaOriginal[i][j] = HospitalApp.INF;
                }
                siguientes[i][j] = -1;
            }
        }
    }
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
                System.out.print(" -> [Error de camino]");
                break;
            }
            System.out.print(" -> " + nombresNodos[actual]);
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
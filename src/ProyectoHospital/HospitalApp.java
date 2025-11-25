package ProyectoHospital;

import InfoHospital.DatosHospital;
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
                    hospital.imprimirRuta("Recepcion", destinoR);
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




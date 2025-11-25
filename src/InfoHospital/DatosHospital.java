
package InfoHospital;

import ProyectoHospital.GrafoHospital;


public class DatosHospital {
    
    public static void cargarMapa(GrafoHospital g) {
        //lugares
        g.limpiar();
        String[] lugares = {
            "Entrada Principal", "Pasillo 1", "Recepcion", "Farmacia", "Auditorio", 
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
        g.agregarArista("Pasillo 1", "Recepcion", 15);
        g.agregarArista("Pasillo 1", "Farmacia", 8);
        g.agregarArista("Pasillo 1", "Auditorio", 8);
        g.agregarArista("Pasillo 1", "Pasillo 2", 21);
        g.agregarArista("Pasillo 1", "Pasillo 3", 21);
        
        g.agregarArista("Pasillo 3", "Consultorio 12", 7);
        g.agregarArista("Pasillo 3", "Sala de Espera", 9);
        g.agregarArista("Sala de Espera", "Recepcion", 5);
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


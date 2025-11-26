package ProyectoHospital;

import InfoHospital.DatosHospital;
import InfoUI.RutaInfo;
import InfoUI.PlanoPanel;
import InfoUI.GrafoAbstractoPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point; // importante: usa java.awt.point
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

//
// esta es la clase principal que crea la ventana
//
public class HospitalSwingUI extends JFrame {

    private PlanoPanel planoPanel; // el panel que dibuja el mapa (la imagen)
    private JTextArea areaResultados; // el cajon de texto de abajo para mostrar la ruta
    private GrafoHospital grafoOriginal; // una copia del grafo sin tocar, para comparar
    private GrafoHospital grafoActual; // el grafo que se modifica con aglomeraciones, etc.
    private JTextField txtOrigen; // el campo de texto para escribir el origen
    private JTextField txtDestino; // el campo de texto para escribir el destino
    private Map<String, Point> mapaCoordenadas = new HashMap<>(); // guarda las coordenadas (x,y) para el plano
    private GrafoAbstractoPanel panelGrafoAbstracto; // el panel que dibuja el grafo de bolitas
    private Map<String, Point> coordsAbstractas = new HashMap<>(); // guarda las coordenadas (x,y) para el grafo abstracto
    private javax.swing.Timer timerAnimacion; // el anima ruta
    private ArrayList<String> rutaParaAnimar; // la lista de pasos que se va a animar
    private int indiceAnimacion; // contador para saber en que paso de la animacion vamos

    // guardamos la ultima ruta exitosa para poder recalcularla
    private String ultimoOrigenExitoso = "";
    private String ultimoDestinoExitoso = "";

    
    // este metodo llena el mapa de coordenadas para el panel del plano (la imagen)
    // cada puntoes una coordenada (x, y) en la imagen
    private void inicializarCoordenadas() {
        mapaCoordenadas.put("Entrada Principal", new Point(870, 450));
        mapaCoordenadas.put("Recepcion", new Point(541, 264)); 
        mapaCoordenadas.put("Farmacia", new Point(705, 445));
        mapaCoordenadas.put("Auditorio", new Point(583, 443));
        mapaCoordenadas.put("Sala de Espera", new Point(453, 249));
        mapaCoordenadas.put("Consultorio 12", new Point(419, 306));
        mapaCoordenadas.put("Imagenologia", new Point(513, 75));
        mapaCoordenadas.put("Urgencias", new Point(607, 65));
        mapaCoordenadas.put("Emergencias Ped.", new Point(677, 176)); 
        mapaCoordenadas.put("Sala Emergencias", new Point(728, 128));
        mapaCoordenadas.put("Estacion enfermeria", new Point(732, 25)); // este es 'enfermeria 1' en el grafo
        mapaCoordenadas.put("Sala solo personal autorizado", new Point(388, 41)); // este es 'salas descanso' en el grafo
        mapaCoordenadas.put("Almacen", new Point(253, 33));
        mapaCoordenadas.put("Anatomia Patologica", new Point(352, 110)); 
        mapaCoordenadas.put("Consultorio 11", new Point(415, 104));
        mapaCoordenadas.put("Vestidor H", new Point(130, 31)); 
        mapaCoordenadas.put("Vestidor M", new Point(154, 117)); 
        mapaCoordenadas.put("Lavanderia", new Point(69, 73));
        mapaCoordenadas.put("Estacion Servicio Cocina", new Point(59, 147));
        mapaCoordenadas.put("Cocina", new Point(76, 220));
        mapaCoordenadas.put("Comedor", new Point(208, 219));
        mapaCoordenadas.put("Baños", new Point(364, 260)); // este es 'banos'
        mapaCoordenadas.put("Enfermeria 2", new Point(179, 292)); 
        mapaCoordenadas.put("PYP", new Point(233, 291));
        mapaCoordenadas.put("Consultorio 5", new Point(41, 278));
        mapaCoordenadas.put("Consultorio 4", new Point(39, 339));
        mapaCoordenadas.put("Consultorio 3", new Point(37, 401));
        mapaCoordenadas.put("Consultorio 2", new Point(39, 453));
        mapaCoordenadas.put("Consultorio 1", new Point(47, 483));
        mapaCoordenadas.put("Laboratorio", new Point(216, 458));
        mapaCoordenadas.put("Consulta Externa", new Point(287, 477));
        mapaCoordenadas.put("Trabajo Social", new Point(339, 421)); 
        mapaCoordenadas.put("Consultorio 8", new Point(337, 467));
        mapaCoordenadas.put("Consultorio 7", new Point(388, 413));
        mapaCoordenadas.put("Consultorio 9", new Point(490, 421));
        mapaCoordenadas.put("Consultorio 10", new Point(493, 476));
        mapaCoordenadas.put("Pasillo 1", new Point(685, 272));
        mapaCoordenadas.put("Pasillo 3", new Point(446, 364));
        mapaCoordenadas.put("Pasillo 2", new Point(416, 161));
        mapaCoordenadas.put("Pasillo 4", new Point(298,254));
        mapaCoordenadas.put("SubPasillo 1", new Point(634, 129));
        mapaCoordenadas.put("SubPasillo 3", new Point(248, 129));
        mapaCoordenadas.put("SubPasillo 4", new Point(142, 374));
        mapaCoordenadas.put("SubPasillo 2", new Point(405, 472));
    }

    // este metodo llena el mapa de coordenadas para el panel del grafo abstracto
    private void inicializarCoordenadasAbstractas() { //en el grafo
        coordsAbstractas.put("Entrada Principal", new Point(105, 905));
        coordsAbstractas.put("Pasillo 1", new Point(310, 520));
        coordsAbstractas.put("Recepcion", new Point(105, 400));
        coordsAbstractas.put("Farmacia", new Point(105, 505));
        coordsAbstractas.put("Auditorio", new Point(200, 750));
        coordsAbstractas.put("Sala de Espera", new Point(250, 310));
        coordsAbstractas.put("Pasillo 3", new Point(410, 500));
        coordsAbstractas.put("Consultorio 12", new Point(400, 300));
        coordsAbstractas.put("Pasillo 2", new Point(480, 600));
        coordsAbstractas.put("Pasillo 4", new Point(500, 350));
        coordsAbstractas.put("Imagenologia", new Point(550, 750));
        coordsAbstractas.put("Urgencias", new Point(300, 950));
        coordsAbstractas.put("SubPasillo 1", new Point(650, 850));
        coordsAbstractas.put("Emergencias Ped.", new Point(900, 950));
        coordsAbstractas.put("Sala Emergencias", new Point(800, 900));
        coordsAbstractas.put("Enfermeria 1", new Point(650, 950)); // este es 'estacion enfermeria' en el plano
        coordsAbstractas.put("SubPasillo 3", new Point(600, 450));
        coordsAbstractas.put("Salas Descanso", new Point(650, 650)); // este es 'sala solo personal' en el plano
        coordsAbstractas.put("Almacen", new Point(750, 700));
        coordsAbstractas.put("Anatomia Patologica", new Point(300, 650));
        coordsAbstractas.put("Consultorio 11", new Point(450, 780));
        coordsAbstractas.put("Vestidor H", new Point(900, 750));
        coordsAbstractas.put("Vestidor M", new Point(750, 550));
        coordsAbstractas.put("Lavanderia", new Point(850, 650));
        coordsAbstractas.put("Estacion Servicio Cocina", new Point(900, 500));
        coordsAbstractas.put("Cocina", new Point(750, 300));
        coordsAbstractas.put("Comedor", new Point(650, 400));
        coordsAbstractas.put("Banos", new Point(480, 250)); // este es 'baños'
        coordsAbstractas.put("Enfermeria 2", new Point(900, 350));
        coordsAbstractas.put("PYP", new Point(600, 300));
        coordsAbstractas.put("SubPasillo 4", new Point(500, 150));
        coordsAbstractas.put("Consultorio 5", new Point(800, 100));
        coordsAbstractas.put("Consultorio 4", new Point(650, 120));
        coordsAbstractas.put("Consultorio 3", new Point(400, 50));
        coordsAbstractas.put("Consultorio 2", new Point(650, 50));
        coordsAbstractas.put("Consultorio 1", new Point(900, 50));
        coordsAbstractas.put("Laboratorio", new Point(400, 80));
        coordsAbstractas.put("Consulta Externa", new Point(400, 150));
        coordsAbstractas.put("Trabajo Social", new Point(350, 200));
        coordsAbstractas.put("Consultorio 8", new Point(250, 50));
        coordsAbstractas.put("SubPasillo 2", new Point(250, 150));
        coordsAbstractas.put("Consultorio 7", new Point(50, 150));
        coordsAbstractas.put("Consultorio 9", new Point(50, 250));
        coordsAbstractas.put("Consultorio 10", new Point(50, 50));
    }

    // este es el constructor de la ventana
    public HospitalSwingUI() {
        // creamos el grafo original (la copia de seguridad)
        grafoOriginal = new GrafoHospital();
        DatosHospital.cargarMapa(grafoOriginal);
        grafoOriginal.ejecutarFloydWarshall(); // calculamos sus rutas
        
        // creamos el grafo actual (el que vamos a modificar)
        grafoActual = new GrafoHospital();
        DatosHospital.cargarMapa(grafoActual);
        grafoActual.ejecutarFloydWarshall(); // calculamos sus rutas
        
        inicializarCoordenadas(); // cargamos las coordenadas del plano
        
        // configuracion basica de la ventana
        setTitle("Sistema de Rutas del Hospital");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // usamos un layout de bordes (norte, sur, este, oeste, centro)
        
        inicializarCoordenadasAbstractas(); // cargamos las coordenadas del grafo abstracto
        
        // creamos el panel del menu (el de la izquierda con botones)
        JPanel panelMenu = crearPanelMenu();
        add(panelMenu, BorderLayout.WEST); // lo ponemos a la izquierda (oeste)
        
        // creamos el panel central con pestañas
        JTabbedPane panelVisual = new JTabbedPane();
        
        // pestaña 1: el plano del hospital
        planoPanel = new PlanoPanel(mapaCoordenadas); // creamos el panel del plano y le pasamos sus coordenadas
        panelVisual.addTab("Plano del Hospital (Ruta)", planoPanel);
        
        // pestaña 2: el grafo abstracto
        panelGrafoAbstracto = new GrafoAbstractoPanel(grafoActual, coordsAbstractas); // creamos el panel del grafo
        JScrollPane scrollGrafo = new JScrollPane(panelGrafoAbstracto); // le ponemos barras de scroll
        panelVisual.addTab("Grafo Abstracto)", scrollGrafo);
        
        add(panelVisual, BorderLayout.CENTER); // añadimos las pestañas al centro de la ventana

        // creamos el area de texto de abajo para los resultados
        areaResultados = new JTextArea(8, 50); 
        areaResultados.setEditable(false); // para que el usuario no pueda escribir ahi
        JScrollPane scrollResultados = new JScrollPane(areaResultados); // le ponemos barras de scroll
        add(scrollResultados, BorderLayout.SOUTH); // lo ponemos abajo (sur)
        
        configurarTimer(); // preparamos el 'reloj' para la animacion
    }

    // este metodo configura el timer
    private void configurarTimer() { //timer del grafo
        // crea un timer que se dispara cada 1000 milisegundos (1 segundo)
        timerAnimacion = new javax.swing.Timer(1000, new ActionListener() {
            
            // esto es lo que pasa cada 1 segundo
            @Override
            public void actionPerformed(ActionEvent e) {
                // si el contador de animacion ya supero el tamaño de la ruta
                if (indiceAnimacion >= rutaParaAnimar.size()) {
                    timerAnimacion.stop(); // paramos el reloj
                    return;
                }
                
                // creamos una lista "parcial" con los pasos de la animacion que van hasta ahora
                ArrayList<String> rutaParcial = new ArrayList<>(
                        rutaParaAnimar.subList(0, indiceAnimacion + 1) // corta la lista desde 0 hasta el paso actual
                );
                
                // le mandamos la ruta parcial al panel del grafo para que la dibuje
                panelGrafoAbstracto.setRuta(rutaParcial);
                indiceAnimacion++; // aumentamos el contador para el siguiente paso
            }
        });
    }

    // este metodo crea el panel de la izquierda con todos los botones
    private JPanel crearPanelMenu() {
        // usamos un gridlayout de 0 filas, 1 columna (para que apile todo verticalmente)
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // un borde para que no este pegado

        // panel de la ruta general
        panel.add(new JLabel("1. Consultar Ruta General:"));
        panel.add(new JLabel("Origen:"));
        txtOrigen = new JTextField(); // sin texto por defecto
        panel.add(txtOrigen);
        panel.add(new JLabel("Destino:"));
        txtDestino = new JTextField(); // sin texto por defecto
        panel.add(txtDestino);
        JButton btnBuscar = new JButton("Buscar Ruta");
        panel.add(btnBuscar);

        // panel desde informacion (recepcion)
        panel.add(new JSeparator()); // una linea gris para separar
        panel.add(new JLabel("2. ¿A Donde voy? (Desde recepcion):"));
        JTextField txtDestinoR = new JTextField();
        panel.add(txtDestinoR);
        JButton btnBuscarR = new JButton("Buscar desde recepcion");
        panel.add(btnBuscarR);

        // mecanismos de actualizacion botones
        panel.add(new JSeparator());
        panel.add(new JLabel("Reportar evento para aglomeracion o emergencia:"));
        panel.add(new JLabel("Lugar A:"));
        JTextField txtLugarA = new JTextField();
        panel.add(txtLugarA);
        panel.add(new JLabel("Lugar B:"));
        JTextField txtLugarB = new JTextField();
        panel.add(txtLugarB);
        JButton btnAglomeracion = new JButton("3. Reportar Aglomeracion (x2)");
        panel.add(btnAglomeracion);
        JButton btnEmergencia = new JButton("4. Reportar Emergencia (x0.5)");
        panel.add(btnEmergencia);
        JButton btnColapsoHospital = new JButton("5. Colapso en el hospital(Pesos aleatorios)");
        panel.add(btnColapsoHospital);
        panel.add(new JSeparator());
        panel.add(new JLabel("7. Bloquear Nodos (separados por coma):"));
        // reset
        panel.add(new JSeparator());
        JButton btnReset = new JButton("Resetear Grafo al Original");
        panel.add(btnReset);
        
        // accion del boton "buscar ruta"
        btnBuscar.addActionListener(e -> {
            // llama al metodo principal.
            mostrarRuta(txtOrigen.getText(), txtDestino.getText(), false);
        });

        // accion del boton "buscar desde recepcion"
        btnBuscarR.addActionListener(e -> {
            String destino = txtDestinoR.getText();
            // pone "recepcion" y el destino en los campos de texto principales
            txtOrigen.setText("Recepcion");
            txtDestino.setText(destino);
            // llama a mostrar ruta, sin comparar
            mostrarRuta("Recepcion", destino, false); 
        });


        // accion del boton "reportar aglomeracion"
        btnAglomeracion.addActionListener(e -> {
            // aplica el cambio al grafo actual
            grafoActual.actualizarPeso(txtLugarA.getText(), txtLugarB.getText(), 2.0);
            
            panelGrafoAbstracto.repaint(); 
            
            //  revisa si habia una ruta guardada en memoria
            if (!ultimoOrigenExitoso.isEmpty() && !ultimoDestinoExitoso.isEmpty()) {
                // si habia una, la recalculamos
                areaResultados.append("\n\n Aglomeración reportada. \n");
                
                txtOrigen.setText(ultimoOrigenExitoso);
                txtDestino.setText(ultimoDestinoExitoso);
                
                mostrarRuta(ultimoOrigenExitoso, ultimoDestinoExitoso, true); // true = sí comparar
            } else {
                // si no habia ruta en memoria, solo avisamos que el grafo cambio
                areaResultados.setText("Aglomeración reportada. Mostrando ruta afectada:\n");
                txtOrigen.setText(txtLugarA.getText());
                txtDestino.setText(txtLugarB.getText());
                
                mostrarRuta(txtLugarA.getText(), txtLugarB.getText(), false);
            }
        });
        
        // accion del boton "reportar emergencia"
        btnEmergencia.addActionListener(e -> {
            // aplica el cambio en el grafo 'actual', multiplicando por 0.5 (mas rapido)
            grafoActual.actualizarPeso(txtLugarA.getText(), txtLugarB.getText(), 0.5);
            panelGrafoAbstracto.repaint(); // redibuja el grafo
            
            //revisa si hay ruta en memoria
            if (!ultimoOrigenExitoso.isEmpty() && !ultimoDestinoExitoso.isEmpty()) {
                areaResultados.append("\n\nEmergencia reportada. Recalculando ruta\n");
                txtOrigen.setText(ultimoOrigenExitoso);
                txtDestino.setText(ultimoDestinoExitoso);
                // recalcula y compara
                mostrarRuta(ultimoOrigenExitoso, ultimoDestinoExitoso, true);
            } else {
                txtOrigen.setText(txtLugarA.getText());
                txtDestino.setText(txtLugarB.getText());
                
                mostrarRuta(txtLugarA.getText(), txtLugarB.getText(), false);
            }
        });

       

        // accion del boton "caos"
        btnColapsoHospital.addActionListener(e -> {
            // llama al metodo que pone pesos aleatorios
            grafoActual.simularPesosAleatorios(50); 
            panelGrafoAbstracto.repaint();
            
            JOptionPane.showMessageDialog(this,
                    "Pesos aleatorios aplicados",
                    "Simulacion: caos en el hospital",
                    JOptionPane.INFORMATION_MESSAGE);
            
            if (!ultimoOrigenExitoso.isEmpty() && !ultimoDestinoExitoso.isEmpty()) {
                areaResultados.append("\n\nColapso en el hopspital!\n");
                txtOrigen.setText(ultimoOrigenExitoso);
                txtDestino.setText(ultimoDestinoExitoso);
                mostrarRuta(ultimoOrigenExitoso, ultimoDestinoExitoso, true);
            } else {
                txtOrigen.setText(txtLugarA.getText());
                txtDestino.setText(txtLugarB.getText());
                
                mostrarRuta(txtLugarA.getText(), txtLugarB.getText(), false);
            }
        });
       
        // accion del boton "resetear grafo"
        btnReset.addActionListener(e -> {
            // vuelve a cargar los datos originales en el grafo 'actual'
            DatosHospital.cargarMapa(grafoActual);
            // va a recalcular todas las rutas desde cero
            grafoActual.ejecutarFloydWarshall();
 
            areaResultados.setText("GRAFO RESTAURADO AL ESTADO ORIGINAL");
            planoPanel.limpiarRuta(); // borra el dibujo del plano
            panelGrafoAbstracto.limpiarRuta(); // borra el dibujo del grafo
            panelGrafoAbstracto.repaint(); 

            ultimoOrigenExitoso = "";
            ultimoDestinoExitoso = "";
            txtOrigen.setText("");
            txtDestino.setText("");
        });

        return panel; 
    } 

    // este es el metodo principal que busca la ruta y la muestra en todos lados
    private void mostrarRuta(String origen, String destino, boolean mostrarComparacion) {

        // si la animacion anterior esta corriendo, la paramos
        if (timerAnimacion.isRunning()) {
            timerAnimacion.stop();
        }

        // si el origen o destino estan vacios, muestra error
        if (origen.isEmpty() || destino.isEmpty()) {
            if (mostrarComparacion) {
                // si estabamos comparando, añade el error al texto existente
                areaResultados.append("\nError: El origen y el destino no pueden estar vacios para recalcular.");
            } else {
                // si era una busqueda nueva, limpia el texto y pone el error
                areaResultados.setText("Error: El origen y el destino no pueden estar vacios.");
            }
            planoPanel.limpiarRuta(); // borra dibujo del plano
            panelGrafoAbstracto.limpiarRuta(); // borra dibujo del grafo
            return; // no sigas
        }

        //le pedimos al grafo actual que calcule la ruta
        RutaInfo rutaActual = grafoActual.obtenerRuta(origen, destino);


        // revisamos si hay que mostrar la comparacion
        if (mostrarComparacion) {
            // si es true, buscamos la misma ruta pero en el grafo original
            RutaInfo rutaOriginal = grafoOriginal.obtenerRuta(origen, destino);

            areaResultados.append("RUTA NORMAL (ORIGINAL)\n"); // añade este titulo
            if (!rutaOriginal.existe) {
                areaResultados.append("Ruta no encontrada.\n");
            } else {
                // imprime los datos de la ruta original
                areaResultados.append("Tiempo: " + rutaOriginal.distanciaTotal + " seg\n");
                areaResultados.append("Recorrido: " + String.join(" ",rutaOriginal.nodos) + "\n");
            }

            areaResultados.append("\nNUEVA RUTA (ACTUALIZADA)\n"); // añade el titulo para la ruta nueva

        } else {
            // si es false (busqueda normal)
            areaResultados.setText(""); 
            areaResultados.append("RUTA CALCULADA\n");
        }

        // mostramos el resultado de la ruta actual
        if (!rutaActual.existe) {
            // si el grafo dice que no existe (porque esta bloqueada)
            areaResultados.append("Ruta no encontrada (Bloqueada!).\n");
            planoPanel.limpiarRuta();
            panelGrafoAbstracto.limpiarRuta();
            
            // si la ruta se bloqueo, borramos la memoria
            ultimoOrigenExitoso = "";
            ultimoDestinoExitoso = "";
            
        } else {
            // si la ruta si existe
            // la imprimimos en el area de texto
            areaResultados.append("Tiempo: " + rutaActual.distanciaTotal + " seg.\n");
            areaResultados.append("Recorrido: " + String.join("  ", rutaActual.nodos) + "\n");

            //se dibuja en el panel
            planoPanel.setRuta(rutaActual.nodos);

            // animacion
            rutaParaAnimar = rutaActual.nodos; // le damos la lista de pasos
            indiceAnimacion = 0; // reiniciamos el contador de la animacion
            panelGrafoAbstracto.limpiarRuta(); // limpiamos el dibujo anterior
            timerAnimacion.start(); // !empezamos la animacion!
            
            // se guarda en la memoria
            ultimoOrigenExitoso = origen;
            ultimoDestinoExitoso = destino;
        }
    }
    
    // mainnn
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HospitalSwingUI gui = new HospitalSwingUI(); // crea nuestra ventana
            gui.setVisible(true); // la hace visible
        });
    }
}
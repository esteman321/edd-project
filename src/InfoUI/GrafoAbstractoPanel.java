package InfoUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;
import ProyectoHospital.GrafoHospital;

public class GrafoAbstractoPanel extends JPanel {

    private GrafoHospital hospital; // grafo actual
    private Map<String, Point> coordsAbstractas; 
    private ArrayList<String> rutaNodos; // ruta a dibujar

    // pincel para las olineas bloqueadas
    private final Stroke lineaBloqueada;

    public GrafoAbstractoPanel(GrafoHospital grafo, Map<String, Point> coords) {
        this.hospital = grafo;
        this.coordsAbstractas = coords;
        this.rutaNodos = new ArrayList<>();
        setBackground(Color.WHITE);
        // estilo de la linea
        lineaBloqueada = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
                                         0, new float[]{9}, 0);
    }

    public void setRuta(ArrayList<String> nodos) {
        this.rutaNodos = (nodos != null) ? nodos : new ArrayList<>();
        repaint(); //dibuja de nuevo
    }

    public void limpiarRuta() {
        this.rutaNodos.clear();
        repaint(); // dibuja de nuevo
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // dibuja el grafo base
        dibujarGrafoBase(g2d);

        // dibuja la linea
        dibujarRutaAnimada(g2d);
    }

    //dibuja el estado actual del nodo
    private void dibujarGrafoBase(Graphics2D g) {
        String[] nombres = hospital.getNombresNodos();
        int[][] matrizDist = hospital.getDistancias(); // matriz despues de aplicarle algun mecanismo
        int[][] matrizOrig = hospital.getMatrizAdyacenciaOriginal(); //matriz original
        int INF = hospital.getINF();
        for (int i = 0; i < hospital.getNumVertices(); i++) { //dibujar aristas del grafo
            for (int j = i + 1; j < hospital.getNumVertices(); j++) {//solo se dibuja si la arista existia anteriormente
                if (matrizOrig[i][j] != 0 && matrizOrig[i][j] != INF) {
                    Point p1 = coordsAbstractas.get(nombres[i]);
                    Point p2 = coordsAbstractas.get(nombres[j]);
                    if (p1 == null || p2 == null) continue;

                    // revisa el estado actual de la arista
                    if (matrizDist[i][j] == INF) {
                        // esta arista esta bloqueada
                        g.setStroke(lineaBloqueada);
                        g.setColor(Color.RED);
                    } else {
                        g.setStroke(new BasicStroke(1)); //arista 
                        g.setColor(Color.LIGHT_GRAY);
                    }
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    //dibujar
                    String pesoStr;
                    if (matrizDist[i][j] == INF) {
                        pesoStr = "X"; // si esta bloqueado 
                        g.setColor(Color.RED);
                    } else {
                        pesoStr = String.valueOf(matrizDist[i][j]); // El peso actual
                        g.setColor(Color.BLACK);
                    }
                    
                    
                    int midX = (p1.x + p2.x) / 2; // punto medio de la linea
                    int midY = (p1.y + p2.y) / 2;
                    
                    // dibuja el texto
                    g.drawString(pesoStr, midX, midY);
                }
            }
        }

    //dibujo de nodos
        g.setStroke(new BasicStroke(1));
        FontMetrics fm = g.getFontMetrics();
        
        for (String nombre : nombres) {
            Point p = coordsAbstractas.get(nombre);
            if (p != null) {
                // dibujar el circulo
                g.setColor(new Color(220, 240, 255)); 
                g.fillOval(p.x - 15, p.y - 15, 30, 30);
                g.setColor(Color.DARK_GRAY); 
                g.drawOval(p.x - 15, p.y - 15, 30, 30);
                
                
                g.setColor(Color.BLACK);
                g.drawString(nombre, p.x - fm.stringWidth(nombre) / 2, p.y + fm.getAscent() / 2 - 2);
                
                
            }
        }
    }

    //dibujo de ruta animada
    private void dibujarRutaAnimada(Graphics2D g) {
        if (rutaNodos.isEmpty()) {
            return;
        }

        g.setStroke(new BasicStroke(3)); 

        for (int i = 0; i < rutaNodos.size() - 1; i++) {
            Point pA = coordsAbstractas.get(rutaNodos.get(i));
            Point pB = coordsAbstractas.get(rutaNodos.get(i + 1));
            if (pA == null || pB == null) continue;

            g.setColor(Color.RED);
            g.drawLine(pA.x, pA.y, pB.x, pB.y);
        }

        for (String nombre : rutaNodos) {
            Point p = coordsAbstractas.get(nombre);
            if (p != null) {
                g.setColor(Color.BLUE);
                g.fillOval(p.x - 7, p.y - 7, 14, 14);
            }
        }

        Point pFinal = coordsAbstractas.get(rutaNodos.get(rutaNodos.size() - 1));
        if (pFinal != null) {
            g.setColor(Color.GREEN);
            g.fillOval(pFinal.x - 7, pFinal.y - 7, 14, 14);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // se calcula le tamaño en base a el tamaño del grafo
        if (coordsAbstractas.isEmpty()) {
            return new Dimension(1000, 1000); // Default
        }
        int maxX = 0;
        int maxY = 0;
        for (Point p : coordsAbstractas.values()) {
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }
        return new Dimension(maxX + 50, maxY + 50);
    }
}
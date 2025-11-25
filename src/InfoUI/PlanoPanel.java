package InfoUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
/**
 *
 * @author Asus
 */
public class PlanoPanel extends JPanel{
    private BufferedImage planoImagen;
    private ArrayList<String> rutaNodos;
    private Map<String, Point> mapaCoordenadas; 

    public PlanoPanel(Map<String, Point> mapaCoordenadas) {
        this.mapaCoordenadas = mapaCoordenadas;
        this.rutaNodos = new ArrayList<>();
        
        try {
            // se carga la imagen del plano del hospital
            planoImagen = ImageIO.read(new File("plano proyecto final act.jpeg"));
        } catch (IOException e) {
            System.err.println("Error no hay imagen" + e.getMessage());
            planoImagen = null;
        }
    }

    //actualiza la ruta que toma el grafo
    public void setRuta(ArrayList<String> nodos) {
        this.rutaNodos = (nodos != null) ? nodos : new ArrayList<>();
        repaint(); //redibuja
    }
    
    //limpia la ruta dibujada
    public void limpiarRuta() {
        this.rutaNodos.clear();
        repaint();
    }

    //dibujo del swing
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    //todo esto dibuja el fondo
        if (planoImagen != null) {
            // dibuja la imagen escalada
            g.drawImage(planoImagen, 0, 0, this);
        } else {
            // si no hay imagen el fondo lo dibuja gris
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("Error: no se pudo cargar la imagen'", 50, 50);
        }

        // si existe una ruta la dibuja
        if (rutaNodos.isEmpty()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(4)); 

        for (int i = 0; i < rutaNodos.size() - 1; i++) {
            String nodoA = rutaNodos.get(i);
            String nodoB = rutaNodos.get(i + 1);

            Point pA = mapaCoordenadas.get(nodoA);
            Point pB = mapaCoordenadas.get(nodoB);

            if (pA == null || pB == null) {
                System.err.println("Coordenadas no encontradas para: " + nodoA + " o " + nodoB);
                continue;
            }

            // color de la linea de la ruta
            g2d.setColor(Color.PINK);
            g2d.drawLine(pA.x, pA.y, pB.x, pB.y);

            //dibuja un circulo en el nodo
            g2d.setColor(Color.BLUE);
            g2d.fillOval(pA.x - 7, pA.y - 7, 14, 14);
        }
        
        // dibuja el circulo de destinos
        Point pFinal = mapaCoordenadas.get(rutaNodos.get(rutaNodos.size() - 1));
        if (pFinal != null) {
            g2d.setColor(Color.GREEN);
            g2d.fillOval(pFinal.x - 7, pFinal.y - 7, 14, 14);
        }
    }
    
            public Dimension getPreferredSize() {
        if (planoImagen != null) {
            // el panel es el tamaño de la imagen
            return new Dimension(planoImagen.getWidth(), planoImagen.getHeight());
        } else {
            return super.getPreferredSize(); //si la imagen falla, se coloca un default
        }
            }
}
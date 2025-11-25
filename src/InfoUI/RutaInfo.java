/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfoUI;

import java.util.ArrayList;

/**
 *
 * @author Asus
 */
public class RutaInfo {
    public int distanciaTotal;
    public ArrayList<String> nodos;
    public boolean existe;

    public RutaInfo(int distancia, ArrayList<String> nodos, boolean existe) {
        this.distanciaTotal = distancia;
        this.nodos = nodos;
        this.existe = existe;
    }
}

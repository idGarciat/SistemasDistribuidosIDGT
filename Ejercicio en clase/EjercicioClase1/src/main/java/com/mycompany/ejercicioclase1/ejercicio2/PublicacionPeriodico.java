/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicioclase1.ejercicio2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ismae
 */
public class PublicacionPeriodico extends ABPublicaciones{
    
    String fecha;
    //public ArrayList<ABPublicaciones> listasuplementos = new ArrayList<ABPublicaciones>();
    public List<Object> suplementos = new ArrayList<>();

    public PublicacionPeriodico(String nombre, String fecha) {
        super(nombre);
        this.fecha = fecha;
    }
    
}

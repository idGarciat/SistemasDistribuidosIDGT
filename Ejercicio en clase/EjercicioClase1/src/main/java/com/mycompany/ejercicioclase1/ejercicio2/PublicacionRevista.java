/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicioclase1.ejercicio2;

/**
 *
 * @author ismae
 */
public class PublicacionRevista extends ABPublicaciones{
    int año;
    String mes;
    ETipoRevista tiporevista;

    public PublicacionRevista(String nombre, int año, String mes, ETipoRevista tiporevista) {
        super(nombre);
        this.año = año;
        this.mes = mes;
        this.tiporevista = tiporevista;
    }
    
}

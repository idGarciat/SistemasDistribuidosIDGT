/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicioclase1.ejercicio2;
import java.util.ArrayList;


/**
 *
 * @author PC1
 */
public class Biblioteca {
    String Nombre;
    int tamanio;
    public ArrayList<Armario> listaArmarios = new ArrayList<Armario>();

    public Biblioteca(String Nombre, int tamanio) {
        this.Nombre = Nombre;
        this.tamanio = tamanio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public ArrayList<Armario> getListaArmarios() {
        return listaArmarios;
    }

    public void setListaArmarios(ArrayList<Armario> listaArmarios) {
        this.listaArmarios = listaArmarios;
    }
    

}

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
public class Armario {
    int codigo;
    public ArrayList<ABPublicaciones> listapublicaciones = new ArrayList<ABPublicaciones>();
    EArmarios tipoArmario;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public ArrayList<ABPublicaciones> getListapublicaciones() {
        return listapublicaciones;
    }

    public void setListapublicaciones(ArrayList<ABPublicaciones> listapublicaciones) {
        this.listapublicaciones = listapublicaciones;
    }

    public EArmarios getTipoArmario() {
        return tipoArmario;
    }

    public void setTipoArmario(EArmarios tipoArmario) {
        this.tipoArmario = tipoArmario;
    }

    public Armario(int codigo, EArmarios tipoArmario) {
        this.codigo = codigo;
        this.tipoArmario = tipoArmario;
    }
    
}

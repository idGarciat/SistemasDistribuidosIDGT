/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.figuras1;

/**
 *
 * @author PC1
 */
public class Cuadrado implements IFiguras {

    int lado;

    public int getLados() {
        return lado;
    }

    public void setLados(int lados) {
        this.lado = lados;
    }

    public Cuadrado(int lados) {
        this.lado = lados;
    }

    @Override
    public int area() {
        return lado*lado;
    }

    @Override
    public int perimetro() {
        return 4*lado;
    }
    
    
    
    
}

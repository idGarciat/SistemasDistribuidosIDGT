/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.figuras1;

/**
 *
 * @author PC1
 */
public class Circulo implements IFiguras{

    int radio;

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public Circulo(int radio) {
        this.radio = radio;
    }
    
    @Override
    public int area() {
        return 3 * radio * radio; //deberia ser 3.1416 o usar una libreria que permita usar pi y luego cambiar en la interfaz para que admita floats o double
    }

    @Override
    public int perimetro() {
        return 2 * 3 * radio;
    }
    
}

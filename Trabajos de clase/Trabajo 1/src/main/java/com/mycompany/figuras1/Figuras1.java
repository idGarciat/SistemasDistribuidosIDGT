/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.figuras1;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class Figuras1 {
       
    public static void main(String[] args) {
        int opcion;
        ArrayList<IFiguras> listaFiguras = new ArrayList<IFiguras>();
        Scanner sc = new Scanner(System.in);
        Menup();
        opcion = sc.nextInt(); 
        
        switch (opcion) {
            case 1 -> {
            listaFiguras.add(MenuF());
            Menup();
            }
            case 2 -> {
            
            }
            case 3 -> {
            }
            default -> {
            }
        }
            
    }
    
    static void Menup(){
        System.out.println("1.- Agregar Elemento a la superficie");
        System.out.println("2.- Calcular el area ocupada por los elementos ");
        System.out.println("3.- Salir");
        };


    static IFiguras MenuF(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Figura a agregar:");
        System.out.println("1.-Cuadrado");
        System.out.println("2.-Triangulo");
        System.out.println("3.-Redondo");
        int opcion = sc.nextInt(); 
        switch (opcion) {
            case 1 -> {
                int l;
                System.out.println("Introduzca el lado");
                l = sc.nextInt();
                Cuadrado c1 = new Cuadrado(l);
                return c1;
            }
            case 2 -> {
                System.out.println("Introduzca la base");
                int b = sc.nextInt();
                System.out.println("Introduzca el la altura");
                int h = sc.nextInt();
                Triangulo t1 = new Triangulo(b,h);
                return t1;
            }
            case 3 -> {
                System.out.println("Introduzca el radio del circulo");
                int r = sc.nextInt();
                Circulo c1 = new Circulo(r);
                return c1;
            }
            default -> {
                return MenuF();
            }
        }
    }
}

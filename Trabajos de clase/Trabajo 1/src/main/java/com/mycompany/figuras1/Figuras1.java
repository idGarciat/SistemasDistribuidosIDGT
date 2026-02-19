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
            case 1 -> MenuF();
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


    static void MenuF(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Figura a agregar:");
        System.out.println("1.-Cuadrado");
        System.out.println("2.-Triangulo");
        System.out.println("3.-Redondo");
        int opcion = sc.nextInt(); 

    }
}

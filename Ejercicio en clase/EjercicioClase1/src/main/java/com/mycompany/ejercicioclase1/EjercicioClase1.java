/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicioclase1;

import com.mycompany.ejercicioclase1.ejercicio2.Armario;
import com.mycompany.ejercicioclase1.ejercicio2.Biblioteca;
import com.mycompany.ejercicioclase1.ejercicio2.EArmarios;
import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class EjercicioClase1 {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        int opcion;
        opcion = 0;
        while (opcion > 2 || opcion<=0){ 

            System.out.println(" \n Elija una opcion:");
            System.out.println("1.- Ejercicio 1");
            System.out.println("2.- Ejercicio 2");            
            opcion = sc.nextInt(); 

                switch (opcion) {
                case 1 -> {
                    ejercicio1();
                    break;
                }
                case 2 -> {
                    ejercicio2();
                 break;
                }
            }
        }
        
    }
    static void ejercicio1(){
    //ejercicio1
        Scanner sc = new Scanner(System.in);
        int opcion;
        opcion = 0;
        int n;
        
        Operaciones op = new Operaciones();
        while (opcion > 3 || opcion<=0){
            System.out.println(" \n Elija una opcion:");
            System.out.println("1.-Calcular Fibonacci");
            System.out.println("2.-Calcular Factorial");
            System.out.println("3.-Calcular sumatoria");
            opcion = sc.nextInt(); 
            System.out.println("Introduzca n");
            n = sc.nextInt(); 
            
                switch (opcion) {
                case 1 -> {
                    op.fibonacci(n);
                    break;
                }
                case 2 -> {
                    op.factorial(n);
                    break;
                }
                case 3 -> {
                    op.sumatoria(n);
                    break;
                }
            }     
        }
    }
    
    static void ejercicio2(){
    
    //mostrar
    
    Armario arm = new Armario(1, EArmarios.Madera);
    Biblioteca biblio = new Biblioteca("Biblioteca1", 4);
    biblio.listaArmarios.add(arm);
    
    }
}

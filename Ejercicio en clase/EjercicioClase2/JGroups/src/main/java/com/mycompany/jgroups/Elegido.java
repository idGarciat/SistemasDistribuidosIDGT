/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jgroups;

import java.util.Scanner;
import org.jgroups.ReceiverAdapter;

/**
 *
 * @author PC1
 */
public class Elegido{

    void preguntas(int opcion) {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Elige una opcion \n");
        opcion = sc.nextInt();
        switch (opcion) {
            case 1 -> {
                System.out.print("1. ¿Qué es una variable en programación?\n");
                System.out.print("a) Un valor constante\n");
                System.out.print("b) Un espacio en memoria que almacena datos\n");
                System.out.print("c) Un tipo de bucle\n\n");
            }
            case 2 -> {
                System.out.print("2. ¿Cuál es el resultado de 5 + 3 * 2?\n");
                System.out.print("a) 16\n");
                System.out.print("b) 11\n");
                System.out.print("c) 13\n\n");
            }
            case 3 -> {
                System.out.print("3. ¿Qué estructura se usa para repetir un bloque de código?\n");
                System.out.print("a) if\n");
                System.out.print("b) for\n");
                System.out.print("c) switch\n\n");
            }
            case 4 -> {
                System.out.print("4. ¿Qué tipo de dato se usa para números con decimales en Java?\n");
                System.out.print("a) int\n");
                System.out.print("b) boolean\n");
                System.out.print("c) double\n\n");
            }
            case 5 -> {
                System.out.print("5. ¿Cuál de los siguientes es un operador lógico?\n");
                System.out.print("a) &&\n");
                System.out.print("b) +\n");
                System.out.print("c) =\n\n");
            }

        }

    }

    
}

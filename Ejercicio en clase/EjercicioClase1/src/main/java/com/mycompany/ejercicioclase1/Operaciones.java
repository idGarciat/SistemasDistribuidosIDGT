package com.mycompany.ejercicioclase1;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author PC1
 */
public class Operaciones {
    
    public void fibonacci(int n){
        int numero1 = 0, numero2 = 1;
        for (int i = 1; i <= n; ++i) {
            System.out.print(numero1 + ", ");
            int numero3 = numero1 + numero2;
            numero1 = numero2;
            numero2 = numero3;
        }
    }
    
    public void factorial(int n){
        long factorial = 1;

        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        System.out.println("El factorial de "+n+" es: " + factorial);
    }
    
    public void sumatoria(int n){
        int sumatoria = 0;
        for (int i = 1; i <= n; i++) {
            sumatoria += i;
        }
        System.out.println("La sumatoria es: " + sumatoria);
    }
    
    
}

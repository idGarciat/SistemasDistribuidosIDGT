/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.trabajo5;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Scanner sc =new Scanner(System.in);

        try {
            System.out.println("Elija una operacion:");
            System.out.println("1.- Factorial"
                    + "\n2.-Fibonacci"
                    + "\n3.-Sumatoria");
            int op = sc.nextInt();
            switch(op){
                case 1->{      
                    System.out.println("Mensaje 1 cliente");
                    IOperaciones operacion=(IOperaciones)Naming.lookup("rmi://localhost/Factorial");
                    System.out.println("Mensaje 2 cliente");
                    System.out.println("Introduzca 2 numeros");
                    int num1 = sc.nextInt();
                    int num2 = sc.nextInt();
                    System.out.println(operacion.operacion(num1,num2));
                    System.out.println("Mensaje 3 cliente");
                }
                case 2->{      
                    System.out.println("Mensaje 1 cliente");
                    IOperaciones operacion=(IOperaciones)Naming.lookup("rmi://localhost/Fibonacci");
                    System.out.println("Mensaje 2 cliente");
                    System.out.println("Introduzca 2 numeros");
                    int num1 = sc.nextInt();
                    int num2 = sc.nextInt();
                    System.out.println(operacion.operacion(num1,num2));
                    System.out.println("Mensaje 3 cliente");}
                case 3->{      
                    System.out.println("Mensaje 1 cliente");
                    IOperaciones operacion=(IOperaciones)Naming.lookup("rmi://localhost/Sumatoria");
                    System.out.println("Mensaje 2 cliente");
                    System.out.println("Introduzca 2 numeros");
                    int num1 = sc.nextInt();
                    int num2 = sc.nextInt();
                    System.out.println(operacion.operacion(num1,num2));
                    System.out.println("Mensaje 3 cliente");}
            }

            /*
            System.out.println("Mensaje 1 cliente");
            IOperaciones operacion=(IOperaciones)Naming.lookup("rmi://localhost/Sumatoria");
            System.out.println("Mensaje 2 cliente");
            System.out.println(operacion.operacion(1,2));
            System.out.println("Mensaje 3 cliente");
            */
          } catch (NotBoundException ex) {
              System.getLogger(Cliente.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
          } catch (MalformedURLException ex) {
              System.getLogger(Cliente.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
          } catch (RemoteException ex) {
              System.getLogger(Cliente.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
          }
    }
    
}

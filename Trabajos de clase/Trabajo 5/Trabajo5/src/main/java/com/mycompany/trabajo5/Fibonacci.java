/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trabajo5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class Fibonacci extends UnicastRemoteObject implements IOperaciones{

    
    public Fibonacci() throws RemoteException{
        super();
    }
    
    
    @Override
    public String operacion(int num1) throws RemoteException {
        return fibonacci(num1);
    }
    
    String fibonacci(int num1){
        
        int a = 0, b = 1, suma;
        String texto = "";
        for (int i = 0; i < num1; i++) {
            texto += (a + " ");
            suma = a + b;
            a = b;
            b = suma;
        }
        return texto;
    }
    
}

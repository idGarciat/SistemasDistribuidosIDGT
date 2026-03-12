/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trabajo5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author PC1
 */
public class Factorial  extends UnicastRemoteObject implements IOperaciones{
    
    public Factorial() throws RemoteException{
        super();
    }

    @Override
    public String operacion(int num1) throws RemoteException {
        long factorial = 1;
        for (int i = 2; i <= num1; i++) {
            factorial *= i;
        }
        return "El factorial de "+num1+" es: "+factorial; 
    }
    
}

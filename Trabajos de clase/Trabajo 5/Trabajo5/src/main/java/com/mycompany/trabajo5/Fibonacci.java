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
public class Fibonacci extends UnicastRemoteObject implements IOperaciones{

    
    public Fibonacci() throws RemoteException{
        super();
    }
    
    
    @Override
    public String operacion(int num1, int num2) throws RemoteException {
        return "Returnando el fibonacci";
    }
    
}

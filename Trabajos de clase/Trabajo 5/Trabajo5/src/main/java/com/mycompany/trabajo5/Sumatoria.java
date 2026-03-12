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
public class Sumatoria extends UnicastRemoteObject implements IOperaciones{
    
    public Sumatoria() throws RemoteException{
        //System.out.println("Mensaje 1 sumatoria");
        super();
        //System.out.println("Mensaje 2 sumatoria");
    }

    @Override
    public String operacion(int num1) throws RemoteException {
        //System.out.println("Mensaje 3 sumatoria");
        int sumatoria = 0;

        for (int i = 1; i <= num1; i++) {
            sumatoria += i;
        }
        
        return "La sumatoria es: "+sumatoria;
    }
    
}

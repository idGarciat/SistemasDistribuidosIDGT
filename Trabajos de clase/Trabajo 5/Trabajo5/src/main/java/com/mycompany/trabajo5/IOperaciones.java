/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.trabajo5;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author PC1
 */
public interface IOperaciones extends Remote{
    String operacion(int num1) throws RemoteException;; 

}

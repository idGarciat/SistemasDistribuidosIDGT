/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author PC1
 */
public class Justicia extends UnicastRemoteObject implements IJusticia{

    public Justicia() throws RemoteException{
        super();
    }
    @Override
    public void ConsultarCuentas(String ci, String nombres, String apellidos) {
        //
    }

    @Override
    public void Congelar(Cuenta cuenta, double monto) {
        //
    }
    
}

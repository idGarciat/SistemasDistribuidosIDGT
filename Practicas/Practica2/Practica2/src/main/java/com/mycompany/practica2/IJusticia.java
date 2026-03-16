/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.practica2;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author PC1
 */
public interface IJusticia extends Remote{
    void ConsultarCuentas(String ci,String nombres, String apellidos) throws RemoteException;
    void Congelar(Cuenta cuenta, double monto) throws RemoteException;
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tictactoe;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author ismae
 */
public interface ITICITACATOE extends Remote {
    String unirse() throws RemoteException;
    String jugar(int fila, int col, String jugador) throws RemoteException;
    String obtenerEstado() throws RemoteException;
    String reiniciar() throws RemoteException;

}

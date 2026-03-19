/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.tictactoe;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author ismae
 */
public class ClienteTic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ITICITACATOE servicio = (ITICITACATOE) Naming.lookup("rmi://localhost/TICITACATOE");

            String jugador = servicio.unirse();
            if ("SALA_LLENA".equals(jugador)) {
                System.out.println("La sala ya tiene 2 jugadores conectados.");
                return;
            }

            System.out.println("Conectado como jugador: " + jugador);
            System.out.println("Estado inicial: " + servicio.obtenerEstado());
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.getLogger(ClienteTic.class.getName()).log(System.Logger.Level.ERROR, "Error conectando al servidor", ex);
        }
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicio1;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author PC1
 */
class ClientHandler extends Thread{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    String nombre;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received ="";
        try {
            this.nombre = dis.readUTF();
        while (true) {
            try {
                received = dis.readUTF();
                if (received.equalsIgnoreCase("Exit")) {
                    this.s.close();
                    // Eliminar de la lista al salir
                    Ejercicio1.clientes.remove(this);
                    break;
                }
                

                for (ClientHandler cliente : Ejercicio1.clientes) {
                    if (cliente != this) {
                        cliente.dos.writeUTF(nombre +": " + received);
                    }
                }
            } catch (IOException e) {
                break;
            }
        }
            
        } catch (IOException ex) {
            System.getLogger(ClientHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicio2;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author PC1
 */
class ClientHandler implements Runnable{
    private final Socket s;
    private final DataInputStream dis;
    public final DataOutputStream dos;
    
    private String nombre;
    public int puntuacion = 0;
    public int respuestaActual = -1;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public String getNombre() { return nombre; }

    @Override
    public void run() {
        try {
            this.nombre = dis.readUTF();

            while (true) {
                String recibido = dis.readUTF();

                if (recibido.equalsIgnoreCase("Exit")) {
                    desconectar();
                    break;
                }

                try {
                    this.respuestaActual = Integer.parseInt(recibido);
                    if (Ejercicio2.latchRespuestas != null && Ejercicio2.latchRespuestas.getCount() > 0) {
                        Ejercicio2.latchRespuestas.countDown();
                    }
                } catch (NumberFormatException e) {
                    dos.writeUTF("Envia un numero.");
                }
            }
        } catch (IOException e) {
            desconectar();
        }
    }

    private void desconectar() {
        Ejercicio2.clientes.remove(this);
        if (Ejercicio2.latchRespuestas != null) {
            Ejercicio2.latchRespuestas.countDown(); 
        }
        try { this.s.close(); } catch (IOException ex) {}
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.ejercicio1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class ClienteEjercicio1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Scanner scn = new Scanner(System.in);
            
            InetAddress ip = InetAddress.getByName("26.139.90.215");
            Socket s = new Socket(ip, 5056);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
///////////////////////////////
            System.out.print("Introduce tu nombre para el chat: ");
            String nombreUsuario = scn.nextLine();
            dos.writeUTF(nombreUsuario);
///////////////////////////////
            Thread leerMensajes = new Thread(() -> {
                while (true) {
                    try {
                        String msg = dis.readUTF();
                        System.out.println("\n" + msg);
                        System.out.print("> "); //mandar mensaje
                    } catch (IOException e) {
                        System.out.println("Conexion con el servidor perdida.");
                        break;
                    }
                }
            });
            leerMensajes.start();

            //// HILO PRINCIPAL (Escritura): Envía mensajes al servidor
            System.out.println("--- Bienvenido al Chat ---");
            System.out.print("> ");
            while (true) {
                String tosend = scn.nextLine();
                dos.writeUTF(tosend);

                if (tosend.equalsIgnoreCase("Exit")) {
                    s.close();
                    break;
                }
            }

            scn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

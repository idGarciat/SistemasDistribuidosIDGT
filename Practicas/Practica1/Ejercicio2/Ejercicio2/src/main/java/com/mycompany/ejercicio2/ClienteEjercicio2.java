/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.ejercicio2;

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
public class ClienteEjercicio2 {public static void main(String[] args) {
        try (Scanner scn = new Scanner(System.in)) {
            System.out.print("Nombre: ");
            String nombre = scn.nextLine();

            Socket s = new Socket("localhost", 5056);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            dos.writeUTF(nombre);

            Thread lector = new Thread(() -> {
                try {
                    while (true) {
                        System.out.println(dis.readUTF());
                    }
                } catch (IOException e) {
                    System.out.println("Desconectado.");
                }
            });
            lector.setDaemon(true); 
            lector.start();

            while (true) {
                String tosend = scn.nextLine();
                dos.writeUTF(tosend);
                if (tosend.equalsIgnoreCase("Exit")) break;
            }
            s.close();
        } catch (Exception e) {}
    }
}

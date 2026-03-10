/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.ejercicio3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author ismae
 */
public class ClienteEjercicio3 {

    public static void main(String[] args) {
        try {
            Scanner scn = new Scanner(System.in);
            
            InetAddress ip = InetAddress.getByName("26.107.179.192"); 
            int puerto = 7000;

            Socket s = new Socket(ip, puerto);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("--- Conectado al Servidor de Tareas ---");

            while (true) {
                String mensaje = dis.readUTF();
                System.out.println(mensaje);

                if (mensaje.contains("Escribe la descripción de la tarea:") 
                 || mensaje.contains("Introduce el NÚMERO de la tarea a eliminar:")) {
                    String input = scn.nextLine();
                    dos.writeUTF(input);
                    String respuesta = dis.readUTF();
                    System.out.println(respuesta);
                    continue;
                }

                if (mensaje.contains("Desconectando")) {
                    break;
                }

                String opcion = scn.nextLine();
                dos.writeUTF(opcion);
            }

            s.close();
            scn.close();
            System.out.println("Conexión cerrada.");

        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicio3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author ismae
 */
public class Ejercicio3 {
    // Lista compartida donde se guardan las tareas de todos los clientes
    public static ArrayList<String> listaTareas = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Usamos el puerto 7000 para no chocar con los otros ejercicios
        ServerSocket ss = new ServerSocket(7000);
        System.out.println("--- Servidor de Tareas Iniciado (Puerto 7000) ---");

        while (true) {
            Socket s = ss.accept();
            System.out.println("Cliente conectado al sistema de tareas.");
            
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // Creamos e iniciamos el hilo para este cliente
            ClientHandler handler = new ClientHandler(s, dis, dos);
            handler.start();
        }
    }
}

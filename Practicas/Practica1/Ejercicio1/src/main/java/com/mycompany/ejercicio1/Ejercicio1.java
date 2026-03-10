/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicio1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author PC1
 */
public class Ejercicio1 {
    
    public static ArrayList<ClientHandler> clientes = new ArrayList<ClientHandler>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5056);
        while (true) {
            Socket s = null;
            try {
                s = ss.accept();
                System.out.println("Nuevo cliente conectado: " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                //DataInputStream nombre = new DataInputStream(s.getInputStream());

                

                ClientHandler handler = new ClientHandler(s, dis, dos);
                clientes.add(handler);

                Thread t = new Thread(handler);
                t.start();

            } catch (Exception e) {
                if (s != null) s.close();
                e.printStackTrace();
            }
        }
    }
}

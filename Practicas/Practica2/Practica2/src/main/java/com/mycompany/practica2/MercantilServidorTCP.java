/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.practica2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author PC1
 */
public class MercantilServidorTCP {


    public static void main(String[] args) {
         int port = 5002;
        ServerSocket server;
        try {
            server = new ServerSocket(port);
            System.out.println("Se inicio el servidor con exito");
            
            while (true) {
                Socket client;
                PrintStream toClient;
                client = server.accept(); //conexion entre cliente y servidor
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("Cliente se conecto");
                
                String recibido = fromClient.readLine();
                System.out.println("El cliente envio el mensaje: " + recibido);
                
                //////////////////
                String invertida = new StringBuilder(recibido).reverse().toString();
                
                toClient = new PrintStream(client.getOutputStream());
                toClient.println(invertida);
                
                //client.close();
            }

        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }
   
}

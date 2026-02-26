package com.mycompany.clientetcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner; // Importamos Scanner para leer del teclado

/**
 *
 * @author ismae
 */
public class ClienteTCP {

    public static void main(String[] args) {
       int port = 5002;
        try {
            // Pedimos la cadena al usuario
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce una palabra");
            String mensaje = scanner.nextLine();
            
            Socket client = new Socket("localhost", port);  //Aqui con poner la ip del servidor es suficiente
            PrintStream toServer = new PrintStream(client.getOutputStream());
            BufferedReader fromServer = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            
            //
            toServer.println(mensaje);
            
            String result = fromServer.readLine();
            System.out.println("cadena devuelta por el servidor es: " + result);
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
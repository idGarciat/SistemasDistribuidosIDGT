/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author USUARIO
 */
public class ManejadorClienteSEGIP{

        private Socket socket;
        
        public ManejadorClienteSEGIP(Socket socket) {
            this.socket = socket;
        }
        
        public void procesar() {
            new Thread(() -> {
                try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) 
                {
                    String mensaje = entrada.readLine();
                    System.out.println("Solicitud recibida: " + mensaje);
                    
                    if (mensaje != null && mensaje.startsWith("verificar:")) {
                        String[] partes = mensaje.split(":");
                        if (partes.length == 2) {
                            String[] datos = partes[1].split(",");
                            if (datos.length == 3) {
                                String ci = datos[0];
                                String nombres = datos[1];
                                String apellidos = datos[2];
                                
                                if (ci != null && !ci.isEmpty()) {
                                    salida.println("RESULTADO:ENCONTRADO");
                                    System.out.println("Persona encontrada: " + 
                                            nombres + " " + apellidos + " (CI: " + ci + ")");
                                } else {
                                    salida.println("RESULTADO:NO-ENCONTRADO");
                                    System.out.println("CI invalido: " + ci);
                                }
                            }
                        }
                    }
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error manejando cliente: " + e.getMessage());
                }
            }).start();
        }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicio3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author ismae
 */
public class ClientHandler extends Thread {
    
    final Socket s;
    final DataInputStream dis;
    final DataOutputStream dos;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        try {
            while (true) {
                dos.writeUTF("\n--- GESTIÓN DE TAREAS ---\n"
                           + "1. Ver lista de tareas\n"
                           + "2. Agregar nueva tarea\n"
                           + "3. Eliminar una tarea\n"
                           + "4. Salir\n"
                           + "Seleccione una opción:");

                // Ignorar líneas vacías en el menú
                String opcion = dis.readUTF();
                while (opcion.trim().isEmpty()) {
                    opcion = dis.readUTF();
                }

                if (opcion.equals("1")) {
                    if (Ejercicio3.listaTareas.isEmpty()) {
                        dos.writeUTF("La lista está vacía.");
                    } else {
                        String lista = "TAREAS ACTUALES:\n";
                        for (int i = 0; i < Ejercicio3.listaTareas.size(); i++) {
                            lista += i + ". " + Ejercicio3.listaTareas.get(i) + "\n";
                        }
                        dos.writeUTF(lista);
                    }
                } 
                else if (opcion.equals("2")) {
                    dos.writeUTF("Escribe la descripción de la tarea:");
                    // Ignorar líneas vacías en la descripción
                    String nuevaTarea = dis.readUTF();
                    while (nuevaTarea.trim().isEmpty()) {
                        nuevaTarea = dis.readUTF();
                    }
                    Ejercicio3.listaTareas.add(nuevaTarea.trim());
                    dos.writeUTF("¡Tarea guardada con éxito!");
                }
                else if (opcion.equals("3")) {
                    dos.writeUTF("Introduce el NÚMERO de la tarea a eliminar:");
                    // Ignorar líneas vacías en el número
                    String input = dis.readUTF();
                    while (input.trim().isEmpty()) {
                        input = dis.readUTF();
                    }
                    try {
                        int index = Integer.parseInt(input.trim());
                        if (index >= 0 && index < Ejercicio3.listaTareas.size()) {
                            String eliminada = Ejercicio3.listaTareas.remove(index);
                            dos.writeUTF("Tarea '" + eliminada + "' eliminada.");
                        } else {
                            dos.writeUTF("Error: El número no existe.");
                        }
                    } catch (NumberFormatException e) {
                        dos.writeUTF("Error: Debes ingresar un número válido.");
                    }
                }
                else if (opcion.equals("4") || opcion.equalsIgnoreCase("Exit")) {
                    dos.writeUTF("Desconectando...");
                    break;
                }
                // opción no válida ignorada silenciosamente
            }
        } catch (IOException e) {
            System.out.println("Un cliente se ha desconectado abruptamente.");
        } finally {
            try { s.close(); } catch (IOException e) {}
        }
    }
    
}

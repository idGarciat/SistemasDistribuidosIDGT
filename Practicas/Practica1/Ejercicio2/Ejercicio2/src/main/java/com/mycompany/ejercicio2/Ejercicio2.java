/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicio2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author ismae
 */
public class Ejercicio2 {
public static ArrayList<ClientHandler> clientes = new ArrayList<>();
    public static CountDownLatch latchRespuestas;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5056);
        System.out.println("Servidor iniciado en puerto 5056");

        Thread hiloJuego = new Thread(() -> {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Escribe 'start' y presiona ENTER para empezar.");
                sc.nextLine(); 
                iniciarTrivia();
            } catch (Exception e) {}
        });
        hiloJuego.start();

        while (true) {
            Socket s = null;
            try {
                s = ss.accept();
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                ClientHandler handler = new ClientHandler(s, dis, dos);
                clientes.add(handler);
                new Thread(handler).start();
            } catch (Exception e) {
                if (s != null) s.close();
            }
        }
    }

    private static void iniciarTrivia() throws IOException, InterruptedException {
        String[] preguntas = {
            "Pregunta 1: Cuánto es 1+1 \n1.- 3\n2.- 2",
            "Pregunta 2: Cuánto 3+2 \n1.- 6 \n2.- 5"
        };
        int[] respuestasCorrectas = {2, 2};

        enviarATodos("Iniciando juego...");

        for (int i = 0; i < preguntas.length; i++) {
            latchRespuestas = new CountDownLatch(clientes.size());
            
            enviarATodos(preguntas[i]);
            latchRespuestas.await(); // Espera a que todos envíen su respuesta

            enviarATodos("Puntuaciones:");
            for (ClientHandler cliente : clientes) {
                if (cliente.respuestaActual == respuestasCorrectas[i]) {
                    cliente.puntuacion += 10;
                }
                cliente.respuestaActual = -1; // Resetear para la siguiente
                enviarATodos(cliente.getNombre() + ": " + cliente.puntuacion);
            }
            Thread.sleep(2000); // Pequeña pausa antes de la siguiente pregunta
        }
        enviarATodos("Fin del juego.");
    }

    public static void enviarATodos(String mensaje) {
        for (ClientHandler cliente : clientes) {
            try { cliente.dos.writeUTF(mensaje); } catch (IOException e) {}
        }
    }
}

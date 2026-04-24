package com.mycompany.mavenproject1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSEGIPTCP {

    int PUERTO = 5001;
    ServerSocket serverSocket;

    public ServidorSEGIPTCP() {
        try {
            this.serverSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en puerto " + PUERTO);
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    public void iniciar() {
        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ManejadorClienteSEGIP manejador = new ManejadorClienteSEGIP(clientSocket);
                    manejador.procesar();
                }
            } catch (IOException e) {
                System.err.println("Error aceptando cliente: " + e.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) {
        ServidorSEGIPTCP servidor = new ServidorSEGIPTCP();
        servidor.iniciar();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

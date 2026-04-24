package com.mycompany.mavenproject1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ServidorFinancieroUDP {

    int PUERTO = 5002;
    Map<String, String> deudas;

    public ServidorFinancieroUDP() {
        this.deudas = new HashMap<>();

        deudas.put("1234567", "");
        deudas.put("7654321", "1500.00");
        //deudas.put("1111111", "750.50");
    }

    public void iniciar() {
        new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket(PUERTO);
                System.out.println("Servidor iniciado en puerto " + PUERTO);
                byte[] bufer = new byte[1024];

                while (true) {
                    DatagramPacket paqueteEntrada = new DatagramPacket(bufer, bufer.length);
                    socket.receive(paqueteEntrada);
                    String mensaje = new String(paqueteEntrada.getData(), 0, paqueteEntrada.getLength()).trim();
                    System.out.println("Solicitud recibida: " + mensaje);
                    InetAddress direccionCliente = paqueteEntrada.getAddress();
                    int puertoCliente = paqueteEntrada.getPort();
                    String respuesta = procesarSolicitud(mensaje);
                    byte[] datosSalida = respuesta.getBytes();
                    DatagramPacket paqueteSalida = new DatagramPacket(datosSalida, datosSalida.length, direccionCliente, puertoCliente);
                    socket.send(paqueteSalida);
                    System.out.println("Respuesta enviada: " + respuesta);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private String procesarSolicitud(String mensaje) {
        if (mensaje.startsWith("deuda:")) {
            String ci = mensaje.substring(6).trim();
            String monto = deudas.getOrDefault(ci, "");
            if (monto.isEmpty()) {
                System.out.println("CI " + ci + " sin deuda");
            } else {
                System.out.println("CI " + ci + " con deuda: " + monto);
            }
            return monto;
        }
        return "";
    }

    public static void main(String[] args) {
        ServidorFinancieroUDP servidor = new ServidorFinancieroUDP();
        servidor.iniciar();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

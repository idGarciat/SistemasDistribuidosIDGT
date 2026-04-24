package com.mycompany.mavenproject1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServidorUniversitario extends UnicastRemoteObject implements IUniversitario {

    String SEGIP_HOST = "localhost";
    int SEGIP_PUERTO = 5001;
    String FINANCIERO_HOST = "localhost";
    int FINANCIERO_PUERTO = 5002;

    double PROMEDIO_MINIMO = 70.0;

    public ServidorUniversitario() throws RemoteException {
        super();
    }

    @Override
    public RespuestaBeca solicitarBeca(String ci, String nombres, String apellidos) throws RemoteException {
        System.out.println("Solicitud de beca recibida");
        System.out.println("CI: " + ci);
        System.out.println("Estudiante: " + nombres + " " + apellidos);

        RespuestaBeca respuesta = new RespuestaBeca();
        System.out.println("\nPaso 1: Verificando en SEGIP...");
        boolean validoEnSEGIP = verificarEnSEGIP(ci, nombres, apellidos);

        if (!validoEnSEGIP) {
            respuesta.setAprobado(false);
            respuesta.setMotivo("estudiante no encontrado en SEGIP");
            respuesta.setPromedio(0.0);
            System.out.println("Estudiante NO validado en SEGIP");
            return respuesta;
        }

        System.out.println("Estudiante validado en SEGIP");
        System.out.println("\nPaso 2: Consultando historial en BIENESTAR...");
        ArrayList<Nota> historial = obtenerHistorialBienestar(ci);

        if (historial == null || historial.isEmpty()) {
            respuesta.setAprobado(false);
            respuesta.setMotivo("sin historial academico en bienestar");
            respuesta.setPromedio(0.0);
            System.out.println("Sin historial académico");
            return respuesta;
        }

        double promedio = calcularPromedio(historial);
        respuesta.setPromedio(promedio);
        System.out.println("Promedio calculado: " + promedio);

        if (promedio < PROMEDIO_MINIMO) {
            respuesta.setAprobado(false);
            respuesta.setMotivo("promedio insuficiente (minimo requerido: " + PROMEDIO_MINIMO + ")");
            System.out.println("Promedio insuficiente");
            return respuesta;
        }

        System.out.println("Promedio superior a " + PROMEDIO_MINIMO);
        System.out.println("\nPaso 3: Verificando deuda en FINANCIERO...");
        String deuda = consultarFinanciero(ci);

        if (deuda != null && !deuda.isEmpty()) {
            respuesta.setAprobado(false);
            respuesta.setMotivo("estudiante tiene deuda: " + deuda);
            System.out.println("Estudiante tiene deuda");
            return respuesta;
        }

        System.out.println("Estudiante sin deuda");
        System.out.println("\nPaso 4: Evaluacion final...");
        respuesta.setAprobado(true);
        respuesta.setMotivo("elegible para beca");
        System.out.println("BECA APROBADA");

        return respuesta;
    }

    private boolean verificarEnSEGIP(String ci, String nombres, String apellidos) {
        try (Socket socket = new Socket(SEGIP_HOST, SEGIP_PUERTO); PrintWriter salida = new PrintWriter(socket.getOutputStream(), true); BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String solicitud = "verificar:" + ci + "," + nombres + "," + apellidos;
            System.out.println("Enviando a SEGIP: " + solicitud);
            salida.println(solicitud);
            String respuesta = entrada.readLine();
            System.out.println("Respuesta de SEGIP: " + respuesta);

            return respuesta != null && respuesta.contains("ENCONTRADO");

        } catch (IOException e) {
            System.err.println("Error conectando a SEGIP: " + e.getMessage());
            return false;
        }
    }

    private ArrayList<Nota> obtenerHistorialBienestar(String ci) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            IBienestar bienestarService = (IBienestar) registry.lookup("BienestarService");

            System.out.println("Consultando BIENESTAR RMI para CI: " + ci);
            ArrayList<Nota> historial = bienestarService.obtenerHistorial(ci);
            System.out.println("Notas recibidas: " + historial.size());

            return historial;

        } catch (Exception e) {
            System.err.println("Error consultando Bienestar: " + e.getMessage());
            return null;
        }
    }

    private double calcularPromedio(ArrayList<Nota> notas) {
        if (notas.isEmpty()) {
            return 0.0;
        }
        int suma = 0;
        for (Nota nota : notas) {
            suma += nota.getCalificacion();
        }
        double promedio = suma / notas.size();
        return Math.round(promedio * 100.0) / 100.0;
    }

    private String consultarFinanciero(String ci) {
        try {
            DatagramSocket socket = new DatagramSocket();

            String solicitud = "deuda:" + ci;
            byte[] datosEnvio = solicitud.getBytes();

            InetAddress direccion = InetAddress.getByName(FINANCIERO_HOST);
            DatagramPacket paqueteEnvio = new DatagramPacket(
                    datosEnvio, datosEnvio.length, direccion, FINANCIERO_PUERTO);

            System.out.println("Enviando a FINANCIERO UDP: " + solicitud);
            socket.send(paqueteEnvio);
            byte[] bufferRecepcion = new byte[1024];
            DatagramPacket paqueteRecepcion = new DatagramPacket(
                    bufferRecepcion, bufferRecepcion.length);
            socket.receive(paqueteRecepcion);

            String respuesta = new String(paqueteRecepcion.getData(), 0,
                    paqueteRecepcion.getLength()).trim();
            System.out.println("Respuesta de FINANCIERO: " + (respuesta.isEmpty() ? "(sin deuda)" : respuesta));

            socket.close();
            return respuesta;

        } catch (IOException e) {
            System.err.println("Error consultando Financiero: " + e.getMessage());
            return "";
        }
    }

    public static void iniciarServidor() {
        try {
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("Registro RMI creado en puerto 1099");
            } catch (java.rmi.server.ExportException e) {
                System.out.println("Registro RMI ya existe");
            }

            Registry registry = LocateRegistry.getRegistry(1099);
            ServidorUniversitario servidor = new ServidorUniversitario();
            registry.rebind("UniversitarioService", servidor);

            System.out.println("Servidor registrado como UniversitarioService");

        } catch (Exception e) {
            System.err.println("Error iniciando servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        iniciarServidor();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

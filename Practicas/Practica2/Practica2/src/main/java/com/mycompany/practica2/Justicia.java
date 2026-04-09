/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PC1
 */
public class Justicia extends UnicastRemoteObject implements IJusticia{

    private static String HOST = "localhost";
    private static int PUERTO_MERCANTIL = 5002;
    private static int PUERTO_BCP = 6789;

    public Justicia() throws RemoteException{
        super();
    }

    @Override
    public RespuestaCuenta ConsultarCuentas(String ci, String nombres, String apellidos) {
        RespuestaCuenta respuesta = new RespuestaCuenta(false, "Consulta realizada correctamente");
        ArrayList<Cuenta> cuentas = new ArrayList<>();

        try {
            String mercantilRaw = consultarMercantil(ci);
            cuentas.addAll(parsearCuentas(mercantilRaw, EBanco.MERCANTIL, ci, nombres, apellidos));
        } catch (IOException ex) {
            respuesta.setError(true);
            respuesta.setMensaje("Error consultando Banco Mercantil: " + ex.getMessage());
        }

        try {
            String bcpRaw = consultarBCP(ci);
            cuentas.addAll(parsearCuentas(bcpRaw, EBanco.BCP, ci, nombres, apellidos));
        } catch (IOException ex) {
            respuesta.setError(true);
            if (respuesta.getMensaje() == null || respuesta.getMensaje().isBlank()) {
                respuesta.setMensaje("Error consultando BCP: " + ex.getMessage());
            } else {
                respuesta.setMensaje(respuesta.getMensaje() + " | Error consultando BCP: " + ex.getMessage());
            }
        }

        if (cuentas.isEmpty() && !respuesta.isError()) {
            respuesta.setMensaje("No se encontraron cuentas para la persona consultada");
        }

        respuesta.setCuentas(cuentas);
        return respuesta;
    }

    @Override
    public void Congelar(Cuenta cuenta, double monto) throws RemoteException {
        if (cuenta == null) {
            throw new RemoteException("Debe seleccionar una cuenta válida");
        }
        if (monto <= 0) {
            throw new RemoteException("El monto a congelar debe ser mayor a 0");
        }

        String comando = "CONGELAR:" + cuenta.getNrocuenta() + ":" + monto;
        try {
            String respuestaBanco;
            if (cuenta.getBanco() == EBanco.MERCANTIL) {
                respuestaBanco = enviarTCP(comando, PUERTO_MERCANTIL);
            } else {
                respuestaBanco = enviarUDP(comando, PUERTO_BCP);
            }

            if (!"OK".equalsIgnoreCase(respuestaBanco)) {
                throw new RemoteException("No se pudo congelar en banco " + cuenta.getBanco() + ": " + respuestaBanco);
            }
        } catch (IOException ex) {
            throw new RemoteException("Error al congelar fondos: " + ex.getMessage(), ex);
        }
    }

    private String consultarMercantil(String ci) throws IOException {
        return enviarTCP("BUSCAR:" + ci, PUERTO_MERCANTIL);
    }

    private String consultarBCP(String ci) throws IOException {
        return enviarUDP("BUSCAR:" + ci, PUERTO_BCP);
    }

    private String enviarTCP(String mensaje, int puerto) throws IOException {
        try (Socket socket = new Socket(HOST, puerto);
             PrintStream toServer = new PrintStream(socket.getOutputStream());
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            toServer.println(mensaje);
            return fromServer.readLine();
        }
    }

    private String enviarUDP(String mensaje, int puerto) throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);
            byte[] payload = mensaje.getBytes();
            InetAddress destino = InetAddress.getByName(HOST);
            DatagramPacket peticion = new DatagramPacket(payload, payload.length, destino, puerto);
            socket.send(peticion);

            byte[] buffer = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            socket.receive(respuesta);
            return new String(respuesta.getData(), 0, respuesta.getLength());
        } catch (SocketTimeoutException ex) {
            throw new IOException("Timeout esperando respuesta UDP", ex);
        }
    }

    private List<Cuenta> parsearCuentas(String respuestaRaw, EBanco banco, String ci, String nombres, String apellidos) {
        List<Cuenta> cuentas = new ArrayList<>();
        if (respuestaRaw == null || respuestaRaw.isBlank()) {
            return cuentas;
        }

        String[] segmentos = respuestaRaw.split(":");
        for (String segmento : segmentos) {
            String[] cuentaSaldo = segmento.split("-");
            if (cuentaSaldo.length < 2) {
                continue;
            }

            try {
                String nroCuenta = cuentaSaldo[0].trim();
                double saldo = Double.parseDouble(cuentaSaldo[1].trim());
                cuentas.add(new Cuenta(banco, nroCuenta, ci, nombres, apellidos, saldo));
            } catch (NumberFormatException ignored) {
            }
        }
        return cuentas;
    }
    
}

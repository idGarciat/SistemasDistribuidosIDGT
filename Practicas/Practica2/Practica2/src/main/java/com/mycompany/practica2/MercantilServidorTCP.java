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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC1
 */
public class MercantilServidorTCP {

    private static final Map<String, List<RegistroCuenta>> CUENTAS_POR_CI = inicializarDatos();

    private static Map<String, List<RegistroCuenta>> inicializarDatos() {
        Map<String, List<RegistroCuenta>> datos = new HashMap<>();
        List<RegistroCuenta> juan = new ArrayList<>();
        juan.add(new RegistroCuenta("1515", 5200));
        datos.put("11021654", juan);
        return datos;
    }

    private static String procesar(String mensaje) {
        if (mensaje == null || mensaje.isBlank()) {
            return "";
        }

        String[] partes = mensaje.trim().split(":");
        String operacion = partes[0].toUpperCase();

        if ("BUSCAR".equals(operacion) && partes.length >= 2) {
            String ci = partes[1].trim();
            List<RegistroCuenta> cuentas = CUENTAS_POR_CI.get(ci);
            if (cuentas == null || cuentas.isEmpty()) {
                return "";
            }
            StringBuilder salida = new StringBuilder();
            for (RegistroCuenta cuenta : cuentas) {
                if (salida.length() > 0) {
                    salida.append(":");
                }
                salida.append(cuenta.nroCuenta).append("-").append(cuenta.saldo);
            }
            return salida.toString();
        }

        if ("CONGELAR".equals(operacion) && partes.length >= 3) {
            String nroCuenta = partes[1].trim();
            double monto;
            try {
                monto = Double.parseDouble(partes[2].trim());
            } catch (NumberFormatException ex) {
                return "ERROR:MONTO_INVALIDO";
            }

            for (List<RegistroCuenta> cuentas : CUENTAS_POR_CI.values()) {
                for (RegistroCuenta cuenta : cuentas) {
                    if (cuenta.nroCuenta.equals(nroCuenta)) {
                        if (monto <= 0 || cuenta.saldo < monto) {
                            return "ERROR:SALDO_INSUFICIENTE";
                        }
                        cuenta.saldo -= monto;
                        return "OK";
                    }
                }
            }
            return "ERROR:CUENTA_NO_ENCONTRADA";
        }

        return "ERROR:OPERACION_NO_VALIDA";
    }

    private static class RegistroCuenta {
        String nroCuenta;
        double saldo;

        RegistroCuenta(String nroCuenta, double saldo) {
            this.nroCuenta = nroCuenta;
            this.saldo = saldo;
        }
    }

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
                
                String respuesta = procesar(recibido);
                
                toClient = new PrintStream(client.getOutputStream());
                toClient.println(respuesta);
                
                client.close();
            }

        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }
   
}

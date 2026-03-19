    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.practica2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PC1
 */
public class BCPServerUDP {

  private static final Map<String, List<RegistroCuenta>> CUENTAS_POR_CI = inicializarDatos();

  private static Map<String, List<RegistroCuenta>> inicializarDatos() {
    Map<String, List<RegistroCuenta>> datos = new HashMap<>();
    List<RegistroCuenta> juan = new ArrayList<>();
    juan.add(new RegistroCuenta("657654", 6000));
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

  public static void main (String args[]) { 
    int port=6789;  
    try {
      
      DatagramSocket socketUDP = new DatagramSocket(port);
      byte[] bufer = new byte[1000];

      while (true) {
        // Construimos el DatagramPacket para recibir peticiones
        DatagramPacket peticion =
          new DatagramPacket(bufer, bufer.length);

        // Leemos una petición del DatagramSocket
        socketUDP.receive(peticion);

        System.out.print("Datagrama recibido del host: " +
                           peticion.getAddress());
        System.out.println(" desde enl puerto remoto: " +
                           peticion.getPort());
        
        
        String cadena =new String (peticion.getData(), 0, peticion.getLength());
        String response = procesar(cadena);
        byte[] mensaje = response.getBytes();
              

        DatagramPacket respuesta =
          new DatagramPacket(mensaje, mensaje.length,
                             peticion.getAddress(), peticion.getPort());

        // Enviamos la respuesta, que es un eco
        socketUDP.send(respuesta);
      }

    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }

}

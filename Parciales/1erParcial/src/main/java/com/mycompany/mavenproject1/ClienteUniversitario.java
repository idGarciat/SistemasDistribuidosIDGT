package com.mycompany.mavenproject1;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteUniversitario {
    public void solicitarBeca(String ci, String nombres, String apellidos) {
        try {
            System.out.println("CLIENTE UNIVERSITARIO - SOLICITUD DE BECA");
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            IUniversitario universitarioService = (IUniversitario) registry.lookup("UniversitarioService");
            
            System.out.println("Conectado al servidor universitario");
            System.out.println("Enviando solicitud de beca...\n");
            RespuestaBeca respuesta = universitarioService.solicitarBeca(ci, nombres, apellidos);
            System.out.println("RESULTADO DE SOLICITUD");
            System.out.println("\nEstudiante: " + nombres + " " + apellidos);
            System.out.println("CI: " + ci);
            System.out.println("\nRESULTADO:");
            System.out.println("  Aprobado:  " + (respuesta.isAprobado() ? "SI" : "NO"));
            System.out.println("  Promedio:  " + respuesta.getPromedio());
            System.out.println("  Motivo:    " + respuesta.getMotivo());

        } catch (RemoteException e) {
            System.err.println("Error RemoteException: " + e.getMessage());
            System.err.println("Esta el servidor universitario ejecutandose?");
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.err.println("Error: Servidor no esta registrado en RMI");
            System.err.println("Servicio 'UniversitarioService' no encontrado");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        
        //String ci = "1234567";
        //String nombres = "Ana";
        //String apellidos = "Gomez";
        
        //solicitarBeca(ci, nombres, apellidos);
    }
}

package com.mycompany.mavenproject1;


public class Main {
    
    public static void main(String[] args) {
        System.out.println("SOLICITUD DE BECAS");
        System.out.println("Iniciando servidor SEGIP TCP");
        ServidorSEGIPTCP servidorSEGIP = new ServidorSEGIPTCP();
        servidorSEGIP.iniciar();
        esperar(2000);
        System.out.println("\nIniciando servidor Bienestar RMI");
        new Thread(() -> ServidorBienestarRMI.iniciarServidor()).start();
        esperar(2000);
        System.out.println("\nIniciando servidor Financiero UDP");
        ServidorFinancieroUDP servidorFinanciero = new ServidorFinancieroUDP();
        servidorFinanciero.iniciar();
        esperar(2000);
        System.out.println("\nIniciando servidor Universitario RMI");
        new Thread(() -> ServidorUniversitario.iniciarServidor()).start();
        esperar(2000);
        System.out.println("\nIniciando cliente...");
        esperar(2000);
        
        ClienteUniversitario Cliente = new ClienteUniversitario();
        String ci = "1234567";
        String nombres = "Ana";
        String apellidos = "Gomez";
        Cliente.solicitarBeca(ci, nombres, apellidos);
        
        //String ci2 = "7654321";
        //String nombres2 = "Pedro";
        //String apellidos2 = "Martinez";
        //Cliente.solicitarBeca(ci2, nombres2, apellidos2);

        
        System.out.println("\nSistema completado.");
    }
    
    private static void esperar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

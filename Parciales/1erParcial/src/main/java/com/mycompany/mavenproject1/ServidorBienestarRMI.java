package com.mycompany.mavenproject1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServidorBienestarRMI extends UnicastRemoteObject implements IBienestar {

    Map<String, ArrayList<Nota>> historialEstudiantes;

    public ServidorBienestarRMI() throws RemoteException {
        super();
        this.historialEstudiantes = new HashMap<>();

        ArrayList<Nota> notasAna = new ArrayList<>();
        notasAna.add(new Nota("Matemáticas", 85));
        notasAna.add(new Nota("Física", 85));
        notasAna.add(new Nota("Química", 85));
        notasAna.add(new Nota("Programación", 85));
        notasAna.add(new Nota("Computacion", 85));
        historialEstudiantes.put("1234567", notasAna);
        
        ArrayList<Nota> notasPedro = new ArrayList<>();
        notasPedro.add(new Nota("Matemáticas", 65));
        notasPedro.add(new Nota("Física", 68));
        notasPedro.add(new Nota("Química", 70));
        historialEstudiantes.put("7654321", notasPedro);

    }

    @Override
    public ArrayList<Nota> obtenerHistorial(String ci) throws RemoteException {
        System.out.println("Consulta de historial para CI: " + ci);
        ArrayList<Nota> historial = historialEstudiantes.getOrDefault(ci, new ArrayList<>());
        if (historial.isEmpty()) {
            System.out.println("No hay registro para CI: " + ci);
        } else {
            System.out.println("Historial encontrado: " + historial.size() + " notas");
        }
        return historial;
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
            ServidorBienestarRMI servidor = new ServidorBienestarRMI();
            registry.rebind("BienestarService", servidor);

            System.out.println("Servidor registrado como 'BienestarService'");

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

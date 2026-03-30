/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jgroups;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

/**
 *
 * @author PC1
 */
public class Votacion extends ReceiverAdapter{
    JChannel channel; // Canal para el grupo de comunicación
    private String user_name; // atributo para el usuario
    final List<String> state = new LinkedList<>();

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Votacion(String user_name) {
        this.user_name = user_name;
    }

    public void viewAccepted(View new_view) {
        System.out.println("Usuarios conectados " + new_view +"\n");
        PickStarterRandomly(new_view);
        
    }

    public void receive(Message msg) {
        String line = msg.getSrc() + ": " + msg.getObject();
        System.out.println(line);
        synchronized (state) {
            state.add(line);
        }
    }

    public void getState(OutputStream output) throws Exception {
        synchronized (state) {
            Util.objectToStream(state, new DataOutputStream(output));
        }
    }

    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        List<String> list = (List<String>) Util.objectFromStream(new DataInputStream(input));
        synchronized (state) {
            state.clear();
            state.addAll(list);
        }
        System.out.println("estado recibido (" + list.size() + " mensajes en la historia del chat ):");
        for (String str : list) {
            System.out.println(str);
        }
    }

    public void start() throws Exception {
        // Crear el canal y conectar al grupo
        channel = new JChannel();  // Utiliza UDP por defecto
        channel.setReceiver(this); // El objeto que recibirá los mensajes
        channel.connect("ChatCluster");  // Conecta al grupo ChatCluster
        channel.getState(null, 10000); // Opcional, para obtener estado compartido si lo hay
        eventLoop(); // Comienza a leer y enviar mensajes
        channel.close(); // Cierra el canal cuando se termine
    }  

    private void eventLoop() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print("> ");
                System.out.flush();
                String line = in.readLine().toLowerCase();
                if (line.startsWith("quit") || line.startsWith("exit")) {
                    break; //finaliza el bucle
                }
                line = "[" + user_name + "] " + line;
                // Envía el mensaje al grupo
                Message msg = new Message(null, line) {}; //crea mensaje
                channel.send(msg);  // envia al grupo
            } catch (Exception e) {
            }
        }
    }
    
    void PickStarterRandomly(View CurrentView){
        //System.out.print("Miembros? " + CurrentView.getMembers().toString());
        if (CurrentView.size() >= 2){   //deberian ser 3 porque una votacion de 2 no manches xd
            System.out.println("Hay suficientes participantes para iniciar la votacion.");
            int min = 0;
            int max = CurrentView.size()-1 ;
            // Fórmula: (int)(Math.random() * (max - min + 1) + min)
            int NumRand = (int)(Math.random() * (max - min + 1) + min);
            // Genera un número aleatorio entre 1 y 10 (ambos incluidos)
            Address Pick = CurrentView.get(NumRand);
            System.out.println("El elegido es: " + Pick);
            
        } else {
            System.out.println("No hay suficientes participantes para iniciar la votacion");
        }
        
    }
    
    
    
}

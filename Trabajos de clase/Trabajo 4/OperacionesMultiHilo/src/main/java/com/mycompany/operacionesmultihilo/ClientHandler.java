/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.operacionesmultihilo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author PC1
 */
class ClientHandler extends Thread{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        int n1=0,n2=0,op=0;
        float resultado;
        
        
        while (true) {
            try {
                
                // Ask user what he wants
                dos.writeUTF("Escibir Exit para terminar la conneccion.");
                // receive the answer from client
                received = dis.readUTF();
                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                
                dos.writeUTF("Introduzca el primer numero\n");
                n1 = Integer.parseInt(dis.readUTF());
                dos.writeUTF("Introduzca el segundo numero \n");
                n2 = Integer.parseInt(dis.readUTF());
                dos.writeUTF("Introduzca el numero de la operacion\n"
                        + "1.-suma \n 2.-resta \n 3.-Multiplicacion \n 4.-Division \n 5.-Salir");
                op = Integer.parseInt(dis.readUTF());
                switch(op){
                    case 1: 
                        resultado = n1+n2;
                        dos.writeUTF("El resultado es; " + resultado);
                        break;
                    
                    case 2:
                        resultado = n1-n2;
                        dos.writeUTF("El resultado es; " + resultado);
                        break;
                    
                    case 3:
                        resultado = n1*n2;
                        dos.writeUTF("El resultado es; " + resultado);
                        break;
                    
                    case 4:
                        resultado = n1/n2;
                        dos.writeUTF("El resultado es; " + resultado);
                        break;
                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

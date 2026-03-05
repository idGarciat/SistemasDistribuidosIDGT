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
public class ClientHandler extends Thread{
    //DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    //DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    
    
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
        String toreturn;
        int n1=0,n2=0,op=0;
        float resultado;
        while (true) {
            try {

                // Ask user what he wants
                dos.writeUTF("Introduzca el primer numero\n");
                //        + "Escibir Exit para terminar la conneccion.");

                // receive the answer from client
                n1 = Integer.parseInt(dis.readUTF());
                dos.writeUTF("Introduzca el segundo numero \n");
                n2 = Integer.parseInt(dis.readUTF());
                dos.writeUTF("Introduzca el numero de la operacion \n"
                        + "1.-suma \n 2.-resta \n 3.-Multiplicacion \n 4.-Division");
                op = Integer.parseInt(dis.readUTF());
              

                switch(op){
                    case 1->{
                        resultado = n1+n2;
                        dos.writeUTF("El resultado es; " + resultado);
                    }
                    case 2->{
                        resultado = n1-n2;
                        dos.writeUTF("El resultado es; " + resultado);
                    }
                    case 3->{
                        resultado = n1*n2;
                        dos.writeUTF("El resultado es; " + resultado);
                    }
                    case 4->{
                        resultado = n1/n2;
                        dos.writeUTF("El resultado es; " + resultado);
                    }
                }
                /*
                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                */

                // creating Date object
                //Date date = new Date();
                

                // write on output stream based on the
                // answer from the client
                /*
                switch (received) {

                    case "Date":
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    case "Time":
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
                    
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

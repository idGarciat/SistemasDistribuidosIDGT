/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.operacionesmultihilo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class ClienteMultihilo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {

              String mensaje = dis.readUTF();
              System.out.println(mensaje);

                if(mensaje.contains("resultado") || mensaje.contains("Invalid")){
                    continue;
                }

                String tosend = scn.nextLine();
                dos.writeUTF(tosend);

                if (tosend.equals("Exit")) {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

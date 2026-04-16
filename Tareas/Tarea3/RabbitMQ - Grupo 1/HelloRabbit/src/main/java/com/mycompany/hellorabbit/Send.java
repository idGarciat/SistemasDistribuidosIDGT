/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hellorabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author cdk04
 */

public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory(); // objeto para configurar la conexion
        factory.setHost("localhost"); // se conecta en mi pc
        
        try (Connection connection = factory.newConnection(); // abrimos una conexion
             Channel channel = connection.createChannel()) { // y un canal
             
            channel.queueDeclare(QUEUE_NAME, false, false, false, null); //que hello exista
            String message = "Hello World!"; // mensaje que enviamos 
            
            // envia
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8")); //  "" envio directo, a que cola va, convierte a bytes
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hellorabbit;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 *
 * @author cdk04
 */

public class Receive {
    private final static String QUEUE_NAME = "hello";  //nombre de la cola

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory(); // objeto que sabe como conectarse rabbit
        factory.setHost("localhost"); // le decimos que el server ta en nuestra pc
        
        Connection connection = factory.newConnection(); // abrimos la conexion con rabbit
        Channel channel = connection.createChannel(); // crea un canal dentro de la conexion - por aca viajan los mensajes
          
        channel.queueDeclare(QUEUE_NAME, false, false, false, null); // (name, durable, exclusive, autoDelete, arguments)
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> { // definimos que hacer cuando llegue el mensaje
            String message = new String(delivery.getBody(), "UTF-8"); // mensajes de bytes a texto
            System.out.println(" [x] Received '" + message + "'"); 
        };
        // autoAck ->  el mensaje que recibe se marca como automaticamente y se borra de la cola
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { }); //empieza a escuchar la cola, 
    }
}
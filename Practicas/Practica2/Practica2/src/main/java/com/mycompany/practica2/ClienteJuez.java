/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.practica2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class ClienteJuez {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);

        try {
            // TODO code application logic here
            IJusticia justicia = (IJusticia)Naming.lookup("rmi://localhost/Justicia");
            System.out.println("Introduzca el carnet. \n");
            String ci = sc.nextLine();
            System.out.println("Introduzca los nombres. \n");
            String nombres = sc.nextLine();
            System.out.println("Introduzca los apellidos. \n");
            String apellidos = sc.nextLine();
            justicia.ConsultarCuentas(ci, nombres, apellidos);
            System.out.println("Congelar la cuenta? \n"
                    + "1.-Si"
                    + "2.-No");
            int operacion = sc.nextInt();
            switch (operacion) {
                case 1->{
                    System.out.println("Monto? \n");
                    double monto = sc.nextDouble();
                    justicia.Congelar(cuenta, monto);
                }
                case 2->{}
            
            }


            

            
        } catch (NotBoundException ex) {
            System.getLogger(ClienteJuez.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (MalformedURLException ex) {
            System.getLogger(ClienteJuez.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (RemoteException ex) {
            System.getLogger(ClienteJuez.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
    
}

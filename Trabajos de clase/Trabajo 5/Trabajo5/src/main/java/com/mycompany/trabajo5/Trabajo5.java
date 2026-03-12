/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.trabajo5;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class Trabajo5 {

    public static void main(String[] args) {        
        
        try {
            //System.out.println("Mensaje 1 servidor");
            Sumatoria sumatoria=new Sumatoria();
            Factorial factorial=new Factorial();
            Fibonacci fibonacci=new Fibonacci();
            //System.out.println("Mensaje 2 servidor");
            LocateRegistry.createRegistry(1099); //levantar el servidor de registro;
            //System.out.println("Mensaje 3 servidor");
            Naming.bind("Sumatoria",sumatoria);
            Naming.bind("Factorial",factorial);
            Naming.bind("Fibonacci",fibonacci);
            //System.out.println("Mensaje 4 servidor");
        } catch (RemoteException ex) {
            System.getLogger(Trabajo5.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (AlreadyBoundException ex) {
            System.getLogger(Trabajo5.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (MalformedURLException ex) {
            System.getLogger(Trabajo5.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
}

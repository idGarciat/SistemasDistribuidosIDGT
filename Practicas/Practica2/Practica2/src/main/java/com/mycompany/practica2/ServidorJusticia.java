/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.practica2;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author PC1
 */
public class ServidorJusticia {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            Justicia justicia= new Justicia();
            LocateRegistry.createRegistry(1099); //levantar el servidor de registro;
            Naming.bind("Justicia",justicia);

        } catch (RemoteException ex) {
            System.getLogger(ServidorJusticia.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (AlreadyBoundException ex) {
            System.getLogger(ServidorJusticia.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (MalformedURLException ex) {
            System.getLogger(ServidorJusticia.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
}

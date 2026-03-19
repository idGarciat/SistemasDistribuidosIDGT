/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tictactoe;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author ismae
 */
public class TicTacToe {

    public static void main(String[] args) {
        try {
            TICITACATOE tictactoe =new TICITACATOE();
            LocateRegistry.createRegistry(1099); //levantar el servidor de registro;
            Naming.bind("TICITACATOE",tictactoe);
            
        } catch (RemoteException ex) {
            System.getLogger(TicTacToe.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (AlreadyBoundException ex) {
            System.getLogger(TicTacToe.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (MalformedURLException ex) {
            System.getLogger(TicTacToe.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}

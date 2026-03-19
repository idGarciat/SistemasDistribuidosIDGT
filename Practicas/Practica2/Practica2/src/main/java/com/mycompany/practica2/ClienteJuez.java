/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.practica2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
        Scanner sc = new Scanner(System.in);

        try {
            IJusticia justicia = (IJusticia) Naming.lookup("rmi://localhost/Justicia");
            System.out.println("Introduzca el carnet. \n");
            String ci = sc.nextLine();
            System.out.println("Introduzca los nombres. \n");
            String nombres = sc.nextLine();
            System.out.println("Introduzca los apellidos. \n");
            String apellidos = sc.nextLine();

            RespuestaCuenta respuesta = justicia.ConsultarCuentas(ci, nombres, apellidos);
            if (respuesta.isError()) {
                System.out.println("Se produjo un error: " + respuesta.getMensaje());
                return;
            }

            ArrayList<Cuenta> cuentas = respuesta.getCuentas();
            if (cuentas == null || cuentas.isEmpty()) {
                System.out.println("No se encontraron cuentas. " + respuesta.getMensaje());
                return;
            }

            System.out.println("Cuentas encontradas:");
            for (int i = 0; i < cuentas.size(); i++) {
                Cuenta cuenta = cuentas.get(i);
                System.out.println((i + 1) + ". Banco: " + cuenta.getBanco()
                        + " | Cuenta: " + cuenta.getNrocuenta()
                        + " | Saldo: " + cuenta.getSaldo());
            }

            System.out.println("Congelar la cuenta? \n"
                    + "1.-Si"
                    + "2.-No");
            int operacion = sc.nextInt();

            switch (operacion) {
                case 1 -> {
                    System.out.println("Seleccione el número de cuenta a congelar: \n");
                    int indice = sc.nextInt() - 1;
                    if (indice < 0 || indice >= cuentas.size()) {
                        System.out.println("Selección inválida");
                        return;
                    }

                    System.out.println("Monto? \n");
                    double monto = sc.nextDouble();
                    Cuenta cuenta = cuentas.get(indice);
                    justicia.Congelar(cuenta, monto);
                    System.out.println("Operación de congelamiento ejecutada correctamente");
                }
                case 2 -> System.out.println("Operación finalizada sin congelamiento");
                default -> System.out.println("Operación no válida");
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

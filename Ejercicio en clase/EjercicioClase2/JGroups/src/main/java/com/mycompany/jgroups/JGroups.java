/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.jgroups;

import java.util.Scanner;

/**
 *
 * @author PC1
 */
public class JGroups {

    public static void main(String[] args) {
        try {
            System.setProperty("java.net.preferIPv4Stack", "true");
            Scanner sc = new Scanner(System.in);
            System.out.print("Introduzca su nombre");
            String nombre;
            nombre = sc.next();
            new Votacion(nombre).start();
        } catch (Exception ex) {
            System.getLogger(JGroups.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}

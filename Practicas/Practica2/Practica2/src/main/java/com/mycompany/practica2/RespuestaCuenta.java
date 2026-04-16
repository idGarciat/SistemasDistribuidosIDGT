/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author PC1
 */
public class RespuestaCuenta implements Serializable {
    
    boolean error;
    String mensaje;
    ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>(); 

    public RespuestaCuenta() {
    }

    public RespuestaCuenta(boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(ArrayList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
    
}

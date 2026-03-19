/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

import java.io.Serializable;

/**
 *
 * @author PC1
 */
public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L;

    EBanco banco;
    String nrocuenta, ci, nombres, apellidos;
    double saldo;

    public Cuenta() {
    }

    public Cuenta(EBanco banco, String nrocuenta, String ci, String nombres, String apellidos, double saldo) {
        this.banco = banco;
        this.nrocuenta = nrocuenta;
        this.ci = ci;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.saldo = saldo;
    }

    public EBanco getBanco() {
        return banco;
    }

    public void setBanco(EBanco banco) {
        this.banco = banco;
    }

    public String getNrocuenta() {
        return nrocuenta;
    }

    public void setNrocuenta(String nrocuenta) {
        this.nrocuenta = nrocuenta;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Cuenta{" + "banco=" + banco + ", nrocuenta=" + nrocuenta + ", ci=" + ci + ", nombres=" + nombres + ", apellidos=" + apellidos + ", saldo=" + saldo + '}';
    }
    
}

package com.mycompany.mavenproject1;

import java.io.Serializable;

public class RespuestaBeca implements Serializable {
    public boolean aprobado;
    public String motivo;
    public double promedio;
    
    public RespuestaBeca() {
    }
    
    public RespuestaBeca(boolean aprobado, String motivo, double promedio) {
        this.aprobado = aprobado;
        this.motivo = motivo;
        this.promedio = promedio;
    }
    
    public boolean isAprobado() {
        return aprobado;
    }
    
    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public double getPromedio() {
        return promedio;
    }
    
    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }
}

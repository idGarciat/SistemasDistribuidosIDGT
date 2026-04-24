package com.mycompany.mavenproject1;

import java.io.Serializable;

public class Nota implements Serializable {
    public String materia;
    public int calificacion;
    
    public Nota() {
    }
    
    public Nota(String materia, int calificacion) {
        this.materia = materia;
        this.calificacion = calificacion;
    }
    
    public String getMateria() {
        return materia;
    }
    
    public void setMateria(String materia) {
        this.materia = materia;
    }
    
    public int getCalificacion() {
        return calificacion;
    }
    
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}

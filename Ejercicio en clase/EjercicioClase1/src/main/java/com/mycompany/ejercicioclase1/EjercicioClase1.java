/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicioclase1;

import com.mycompany.ejercicioclase1.ejercicio2.Armario;
import com.mycompany.ejercicioclase1.ejercicio2.Biblioteca;
import com.mycompany.ejercicioclase1.ejercicio2.EArmarios;
import com.mycompany.ejercicioclase1.ejercicio2.ETipoRevista;
import com.mycompany.ejercicioclase1.ejercicio2.PublicacionLibro;
import com.mycompany.ejercicioclase1.ejercicio2.PublicacionPeriodico;
import com.mycompany.ejercicioclase1.ejercicio2.PublicacionRevista;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author PC1
 */
public class EjercicioClase1 {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        int opcion;
        opcion = 0;
        while (opcion > 2 || opcion<=0){ 

            System.out.println(" \n Elija una opcion:");
            System.out.println("1.- Ejercicio 1");
            System.out.println("2.- Ejercicio 2");            
            opcion = sc.nextInt(); 

                switch (opcion) {
                case 1 -> {
                    ejercicio1();
                    break;
                }
                case 2 -> {
                    ejercicio2();
                 break;
                }
            }
        }
        
    }
    static void ejercicio1(){
    //ejercicio1
        Scanner sc = new Scanner(System.in);
        int opcion;
        opcion = 0;
        int n;
        
        Operaciones op = new Operaciones();
        while (opcion > 3 || opcion<=0){
            System.out.println(" \n Elija una opcion:");
            System.out.println("1.-Calcular Fibonacci");
            System.out.println("2.-Calcular Factorial");
            System.out.println("3.-Calcular sumatoria");
            opcion = sc.nextInt(); 
            System.out.println("Introduzca n");
            n = sc.nextInt(); 
            
                switch (opcion) {
                case 1 -> {
                    op.fibonacci(n);
                    break;
                }
                case 2 -> {
                    op.factorial(n);
                    break;
                }
                case 3 -> {
                    op.sumatoria(n);
                    break;
                }
            }     
        }
    }
    
    static void ejercicio2(){
        
        Scanner sc = new Scanner(System.in);    
        int opcion;
        
        System.out.println("Creando una biblioteca");
        Biblioteca biblio = new Biblioteca("Biblioteca1", 4);
        System.out.println("Crear armario? \n 1.-Si \n 2.-No");
        opcion = sc.nextInt();
            while (opcion != 1 || opcion !=2 ){
                switch (opcion){
                    case 1 -> {
                        System.out.println("1.- Madera \n 2.-Metalico");
                        opcion = sc.nextInt();
                        switch(opcion){
                            case 1 -> {
                                Armario arm = new Armario(1, EArmarios.Madera);
                                biblio.listaArmarios.add(arm);
                                crearpublicacion(arm);
                                System.out.println(arm);
                                break;
                            }
                            case 2 -> {
                                Armario arm = new Armario(1, EArmarios.Metalico);
                                biblio.listaArmarios.add(arm);
                                crearpublicacion(arm);
                                System.out.println(arm);
                                break;
                            }
                        }                  
                    }
                    case 2 ->{} 
            }
        }
            System.out.println(biblio);
            guardarjson(biblio);
            
     }
    static void crearpublicacion(Armario arm){
        int opcion;
        Scanner sc = new Scanner(System.in);  
        System.out.println("Crear publicacion? \n 1.-Si \n 2.-No");
        opcion = sc.nextInt();
        switch(opcion){
            case 1 ->{
                System.out.println("Tipo de publicacion: \n 1.-Libro \n 2.-Revista \n3.-Periodico ");
                opcion = sc.nextInt();
                switch(opcion){
                    case 1 ->{
                        System.out.println("Nombre del libro");
                        String nombre = sc.next();
                        System.out.println("Autor del libro");
                        String autor = sc.next();
                        System.out.println("Editorial");
                        String editorial = sc.next();
                        System.out.println("Año");
                        int ano = sc.nextInt();
                        PublicacionLibro publi = new PublicacionLibro(nombre, autor, editorial, ano);
                        arm.listapublicaciones.add(publi);
                    }
                    case 2 ->{
                        System.out.println("Nombre de la revista");
                        String nombre = sc.next();
                        System.out.println("Año");
                        int año = sc.nextInt();
                        System.out.println("Mes (nombre)");
                        String mes = sc.next();
                        System.out.println("Tipo Revista");
                        System.out.println("1.-Tecnica \n 2.-Moda \n 3.-Variedades");
                        opcion = sc.nextInt();
                        switch (opcion){
                            case 1->{
                                PublicacionRevista publi = new PublicacionRevista(nombre, año, mes, ETipoRevista.Tecnica);
                                arm.listapublicaciones.add(publi);
                            }
                            case 2->{
                                PublicacionRevista publi = new PublicacionRevista(nombre, año, mes, ETipoRevista.Moda);
                                arm.listapublicaciones.add(publi);                               
                            }
                            case 3->{
                                PublicacionRevista publi = new PublicacionRevista(nombre, año, mes, ETipoRevista.Variedades);
                                arm.listapublicaciones.add(publi);                                
                            }
                        }  
                    }
                    case 3 ->{
                        System.out.println("Nombre Periodico");
                        String nombre = sc.next();
                        System.out.println("Fecha (texto)");
                        String fecha = sc.next();
                        PublicacionPeriodico publi = new PublicacionPeriodico(nombre, fecha);
                        System.out.println("Agregar Suplementos? \n 1.-Si \n 2.-No");
                        switch(opcion){
                            case 1 ->{
                                System.out.println("En texto porque me da flojera hacer más pero podría aceptar cualquier wea");
                                String suplemento = sc.next();
                                publi.suplementos.add(suplemento);
                            }
                            case 2 ->{}
                        }
                        arm.listapublicaciones.add(publi);
                    }                            
                }                
            }
            case 2 ->{}   
        } 
    }
    
    static void guardarjson(Biblioteca biblio) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(biblio);
        try (FileWriter writer = new FileWriter("mi_biblioteca.json")) {
            writer.write(json);
            System.out.println("\n La wea se guardo con exito");
            System.out.println("Busca el archivo mi_biblioteca.json en la carpeta raíz de tu proyecto.");
        } catch (IOException e) {
            System.out.println("\n Error al guardar el archivo: " + e.getMessage());
        }
    }
}

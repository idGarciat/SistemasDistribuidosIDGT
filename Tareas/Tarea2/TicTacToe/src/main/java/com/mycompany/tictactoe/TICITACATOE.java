/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoe;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author ismae
 */
public class TICITACATOE  extends UnicastRemoteObject implements ITICITACATOE{

    private final char[][] tablero;
    private String turnoActual;
    private String ganador;
    private boolean empate;
    private int movimientos;
    private int jugadoresConectados;

    public TICITACATOE() throws RemoteException{
        super();
        tablero = new char[3][3];
        inicializarTablero();
        turnoActual = "X";
        ganador = "-";
        empate = false;
        movimientos = 0;
        jugadoresConectados = 0;
    }

    @Override
    public synchronized String unirse() throws RemoteException {
        if (jugadoresConectados == 0) {
            jugadoresConectados++;
            return "X";
        }
        if (jugadoresConectados == 1) {
            jugadoresConectados++;
            return "O";
        }
        return "SALA_LLENA";
    }

    @Override
    public synchronized String jugar(int fila, int col, String jugador) throws RemoteException {
        
        tablero[fila][col] = jugador.charAt(0);
        movimientos++;

        if (hayGanador(tablero[fila][col])) {
            ganador = jugador;
        } else if (movimientos == 9) {
            empate = true;
        } else {
            if ("X".equals(turnoActual)) {
                turnoActual = "O";
            } else {
                turnoActual = "X";
            }
        }

        return "OK|" + obtenerEstado();
    }

    @Override
    public synchronized String obtenerEstado() throws RemoteException {
        return "TABLERO=" + serializarTablero()
                + ";TURNO=" + turnoActual
                + ";GANADOR=" + ganador
                + ";EMPATE=" + empate
                + ";JUGADORES=" + jugadoresConectados;
    }

    @Override
    public synchronized String reiniciar() throws RemoteException {
        inicializarTablero();
        turnoActual = "X";
        ganador = "-";
        empate = false;
        movimientos = 0;
        return "OK|" + obtenerEstado();
    }

    private void inicializarTablero() {
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {
                tablero[fila][col] = '-';
            }
        }
    }

    private String serializarTablero() {
        StringBuilder builder = new StringBuilder(9);
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {
                builder.append(tablero[fila][col]);
            }
        }
        return builder.toString();
    }

    private boolean hayGanador(char simbolo) {
        for (int fila = 0; fila < 3; fila++) {
            if (tablero[fila][0] == simbolo && tablero[fila][1] == simbolo && tablero[fila][2] == simbolo) {
                return true;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (tablero[0][col] == simbolo && tablero[1][col] == simbolo && tablero[2][col] == simbolo) {
                return true;
            }
        }

        return (tablero[0][0] == simbolo && tablero[1][1] == simbolo && tablero[2][2] == simbolo)
                || (tablero[0][2] == simbolo && tablero[1][1] == simbolo && tablero[2][0] == simbolo);
    }
    
}

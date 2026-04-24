package com.mycompany.mavenproject1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IBienestar extends Remote {
    ArrayList<Nota> obtenerHistorial(String ci) throws RemoteException;
}

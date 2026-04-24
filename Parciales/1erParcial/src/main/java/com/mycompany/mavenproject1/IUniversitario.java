package com.mycompany.mavenproject1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUniversitario extends Remote {
    RespuestaBeca solicitarBeca(String ci, String nombres, String apellidos) throws RemoteException;
}

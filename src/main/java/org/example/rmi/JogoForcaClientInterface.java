package org.example.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoForcaClientInterface extends Remote {
    void receiveMessage(String sender, String message) throws RemoteException;
}
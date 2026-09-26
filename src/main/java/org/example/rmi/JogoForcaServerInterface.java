package org.example.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoForcaServerInterface extends Remote {
    void registerClient(String username, JogoForcaClientInterface client) throws RemoteException;
    void unregisterClient(String username) throws RemoteException;
    void sendMessage(String sender, String message) throws RemoteException;
}
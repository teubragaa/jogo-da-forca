package org.example.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class JogoForcaClientImpl extends UnicastRemoteObject implements JogoForcaClientInterface {

    public JogoForcaClientImpl() throws RemoteException {
        super();
    }

    @Override
    public void receiveMessage(String sender, String message) throws RemoteException {
        System.out.println("\n[" + sender + "]:\n" + message);
        System.out.print("> Digite seu palpite: ");
    }
}
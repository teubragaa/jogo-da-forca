package org.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class JogoForcaClientImpl extends UnicastRemoteObject implements JogoForcaClientInterface {

    private final String username;

    public JogoForcaClientImpl(String username) throws RemoteException {
        super();
        this.username = username;
    }

    @Override
    public void receiveMessage(String sender, String message) throws RemoteException {
        // Exibe o estado da forca ou as mensagens transmitidas pelo servidor
        if (!sender.equalsIgnoreCase(username)) {
            System.out.println("\n[" + sender + "]:\n" + message);
            System.out.print("> Digite um palpite (letra): "); // Reexibe o prompt
        }
    }
}
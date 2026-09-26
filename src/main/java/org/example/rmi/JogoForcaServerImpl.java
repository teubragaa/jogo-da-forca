package org.example.rmi;

import org.example.client.ViewConsole;
import org.example.model.JogoForca;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JogoForcaServerImpl extends UnicastRemoteObject implements JogoForcaServerInterface {

    private final Map<String, JogoForcaClientInterface> clients = new ConcurrentHashMap<>();
    private final JogoForca jogo = new JogoForca("SISTEMAS");

    public JogoForcaServerImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void registerClient(String username, JogoForcaClientInterface client) throws RemoteException {
        clients.put(username, client);
        jogo.adicionarJogador(username);

        String tela = ViewConsole.formatarTela(
                jogo.montarPalavraOculta(),
                jogo.getLetrasUsadas(),
                jogo.getVidas(),
                jogo.getJogadorDaVez(),
                username + " entrou no jogo!"
        );

        broadcast("ForcaSD", tela);
    }

    @Override
    public synchronized void unregisterClient(String username) throws RemoteException {
        if (clients.remove(username) != null) {
            jogo.removerJogador(username);

            String tela = ViewConsole.formatarTela(
                    jogo.montarPalavraOculta(),
                    jogo.getLetrasUsadas(),
                    jogo.getVidas(),
                    jogo.getJogadorDaVez(),
                    username + " saiu do jogo."
            );

            broadcast("ForcaSD", tela);
        }
    }

    @Override
    public synchronized void sendMessage(String sender, String message) throws RemoteException {
        String status = jogo.processarJogada(sender, message);

        String tela = ViewConsole.formatarTela(
                jogo.montarPalavraOculta(),
                jogo.getLetrasUsadas(),
                jogo.getVidas(),
                jogo.getJogadorDaVez(),
                status
        );

        broadcast("ForcaSD", tela);
    }

    private void broadcast(String sender, String msg) {
        for (Map.Entry<String, JogoForcaClientInterface> entry : clients.entrySet()) {
            try {
                entry.getValue().receiveMessage(sender, msg);
            } catch (Exception e) {
                clients.remove(entry.getKey());
            }
        }
    }
}
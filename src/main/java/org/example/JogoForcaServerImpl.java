package org.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JogoForcaServerImpl extends UnicastRemoteObject implements JogoForcaServerInterface {

    private final Map<String, JogoForcaClientInterface> clients = new ConcurrentHashMap<>();
    private final List<String> ordemJogadores = new ArrayList<>(); // Fila de turnos
    private int indiceJogadorAtual = 0;

    // Estado do Jogo da Forca
    private final String palavraSecreta = "SISTEMAS";
    private final Set<Character> letrasAdivinhadas = new HashSet<>();
    private final Set<Character> letrasErradas = new HashSet<>();
    private int vidasRestantes = 6;
    private boolean jogoFinalizado = false;

    public JogoForcaServerImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void registerClient(String username, JogoForcaClientInterface client) throws RemoteException {
        if (clients.containsKey(username)) {
            throw new RemoteException("Nome do jogador " + username + " já existe ou já está na sala");
        }
        clients.put(username, client);
        ordemJogadores.add(username);
        System.out.println("O jogador " + username + " acessou a sala do jogo");

        broadcastMessage("ForcaSD", username + " entrou na sala!\n" + montarEstadoJogo());
    }

    @Override
    public synchronized void unregisterClient(String username) throws RemoteException {
        if (clients.remove(username) != null) {
            int indexRemovido = ordemJogadores.indexOf(username);
            ordemJogadores.remove(username);

            // Ajusta o índice do turno caso o jogador que saiu estivesse jogando ou antes na fila
            if (!ordemJogadores.isEmpty()) {
                if (indexRemovido <= indiceJogadorAtual) {
                    indiceJogadorAtual = indiceJogadorAtual % ordemJogadores.size();
                }
            }

            System.out.println("Jogador " + username + " saiu da sala");
            broadcastMessage("ForcaSD", username + " saiu da sala.\n" + montarEstadoJogo());
        }
    }

    @Override
    public synchronized void sendMessage(String sender, String message) throws RemoteException {
        if (jogoFinalizado) {
            broadcastMessage("ForcaSD", "O jogo já terminou! Reinicie o servidor para jogar novamente.");
            return;
        }

        // 1. Validação de Turno: Verifica se é a vez de quem enviou
        String jogadorDaVez = obterJogadorDaVez();
        if (!sender.equalsIgnoreCase(jogadorDaVez)) {
            // Notifica apenas a tentativa ou ignora
            broadcastMessage("ForcaSD", "⚠️ Apenas aguarde! É a vez de " + jogadorDaVez + " jogar.");
            return;
        }

        String palpite = message.trim().toUpperCase();

        if (palpite.length() != 1 || !Character.isLetter(palpite.charAt(0))) {
            broadcastMessage("ForcaSD", "Palpite inválido de " + sender + ". Digite apenas uma letra!");
            return;
        }

        char letra = palpite.charAt(0);

        if (letrasAdivinhadas.contains(letra) || letrasErradas.contains(letra)) {
            broadcastMessage("ForcaSD", "A letra '" + letra + "' já foi tentada antes!\n" + montarEstadoJogo());
            return;
        }

        // 2. Processa a jogada
        boolean acertou = false;
        if (palavraSecreta.indexOf(letra) >= 0) {
            letrasAdivinhadas.add(letra);
            acertou = true;
            if (checarVitoria()) {
                jogoFinalizado = true;
                broadcastMessage("ForcaSD", "🎉 PARABÉNS! O jogador " + sender + " acertou a última letra '" + letra + "'!\nPalavra: " + palavraSecreta + "\n" + sender.toUpperCase() + " VENCEU O JOGO!");
                return;
            }
        } else {
            letrasErradas.add(letra);
            vidasRestantes--;
            if (vidasRestantes <= 0) {
                jogoFinalizado = true;
                broadcastMessage("ForcaSD", "☠️ GAME OVER! " + sender + " errou a letra '" + letra + "'. As vidas acabaram!\nA palavra era: " + palavraSecreta);
                return;
            }
        }

        // 3. Alterna o turno para o próximo jogador da lista
        proximoTurno();

        // 4. Notifica todos sobre o resultado e quem é o próximo a jogar
        String msgResultado = acertou ? "👍 " + sender + " ACERTOU a letra '" + letra + "'!" : "❌ " + sender + " ERROU a letra '" + letra + "'!";
        broadcastMessage("ForcaSD", msgResultado + "\n" + montarEstadoJogo());
    }

    private void proximoTurno() {
        if (!ordemJogadores.isEmpty()) {
            indiceJogadorAtual = (indiceJogadorAtual + 1) % ordemJogadores.size();
        }
    }

    private String obterJogadorDaVez() {
        if (ordemJogadores.isEmpty()) return "";
        return ordemJogadores.get(indiceJogadorAtual);
    }

    private void broadcastMessage(String sender, String message) {
        System.out.println("[" + sender + "] " + message);
        for (Map.Entry<String, JogoForcaClientInterface> entry : clients.entrySet()) {
            try {
                entry.getValue().receiveMessage(sender, message);
            } catch (RemoteException e) {
                System.err.println("Erro ao enviar a mensagem para " + entry.getKey() + ". Removendo cliente");
                clients.remove(entry.getKey());
            }
        }
    }

    private String montarEstadoJogo() {
        StringBuilder sb = new StringBuilder();
        sb.append(desenharForca()).append("\n");
        sb.append("Palavra: ").append(montarPalavraOculta()).append("\n");
        sb.append("Letras tentadas: ").append(montarLetrasUsadas()).append("\n");
        sb.append("Vidas do grupo: ").append(vidasRestantes).append("\n");
        sb.append("👉 VEZ DE JOGAR: ").append(obterJogadorDaVez().toUpperCase());
        return sb.toString();
    }

    private String montarPalavraOculta() {
        StringBuilder sb = new StringBuilder();
        for (char c : palavraSecreta.toCharArray()) {
            if (letrasAdivinhadas.contains(c)) {
                sb.append(c).append(" ");
            } else {
                sb.append("_ ");
            }
        }
        return sb.toString().trim();
    }

    private boolean checarVitoria() {
        for (char c : palavraSecreta.toCharArray()) {
            if (!letrasAdivinhadas.contains(c)) {
                return false;
            }
        }
        return true;
    }

    private String montarLetrasUsadas() {
        Set<Character> todas = new TreeSet<>(letrasAdivinhadas);
        todas.addAll(letrasErradas);
        return todas.toString();
    }

    private String desenharForca() {
        switch (vidasRestantes) {
            case 6: return " +---+\n |   |\n     |\n     |\n     |\n=====";
            case 5: return " +---+\n |   |\n O   |\n     |\n     |\n=====";
            case 4: return " +---+\n |   |\n O   |\n |   |\n     |\n=====";
            case 3: return " +---+\n |   |\n O   |\n/|   |\n     |\n=====";
            case 2: return " +---+\n |   |\n O   |\n/|\\  |\n     |\n=====";
            case 1: return " +---+\n |   |\n O   |\n/|\\  |\n/    |\n=====";
            default:return " +---+\n |   |\n O   |\n/|\\  |\n/ \\  |\n=====";
        }
    }
}
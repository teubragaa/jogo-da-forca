package org.example.model;

import java.util.*;

public class JogoForca {
    private final String palavraSecreta;
    private final Set<Character> letrasUsadas = new HashSet<>();
    private final List<String> jogadores = new ArrayList<>();
    private int turnoAtual = 0;
    private int vidas = 6;

    public JogoForca(String palavraSecreta) {
        this.palavraSecreta = palavraSecreta.toUpperCase();
    }

    public synchronized void adicionarJogador(String username) {
        if (!jogadores.contains(username)) {
            jogadores.add(username);
        }
    }

    public synchronized void removerJogador(String username) {
        jogadores.remove(username);
        if (!jogadores.isEmpty()) {
            turnoAtual %= jogadores.size();
        }
    }

    public synchronized String processarJogada(String sender, String palpite) {
        if (jogadores.isEmpty()) return "Nenhum jogador na sala.";

        String jogadorDaVez = jogadores.get(turnoAtual);
        if (!sender.equalsIgnoreCase(jogadorDaVez)) {
            return "⚠️ Calma " + sender + "! É a vez de " + jogadorDaVez;
        }

        String texto = palpite.trim().toUpperCase();
        if (texto.isEmpty()) return "";

        char letra = texto.charAt(0);

        if (!letrasUsadas.contains(letra)) {
            letrasUsadas.add(letra);
            if (palavraSecreta.indexOf(letra) < 0) {
                vidas--;
            }
        }

        // Alterna o turno para o próximo jogador
        turnoAtual = (turnoAtual + 1) % jogadores.size();

        return "Palpite de " + sender + ": '" + letra + "'";
    }

    public String montarPalavraOculta() {
        StringBuilder sb = new StringBuilder();
        for (char c : palavraSecreta.toCharArray()) {
            sb.append(letrasUsadas.contains(c) ? c + " " : "_ ");
        }
        return sb.toString().trim();
    }

    public String getJogadorDaVez() {
        return jogadores.isEmpty() ? "" : jogadores.get(turnoAtual);
    }

    public int getVidas() {
        return vidas;
    }

    public Set<Character> getLetrasUsadas() {
        return letrasUsadas;
    }
}
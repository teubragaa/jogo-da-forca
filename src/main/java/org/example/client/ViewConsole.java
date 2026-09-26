package org.example.client;

import java.util.Set;

public class ViewConsole {

    public static String formatarTela(String palavraOculta, Set<Character> letrasUsadas, int vidas, String jogadorDaVez, String mensagemStatus) {
        StringBuilder sb = new StringBuilder();

        if (mensagemStatus != null && !mensagemStatus.isEmpty()) {
            sb.append("📢 ").append(mensagemStatus).append("\n");
        }

        sb.append(desenharForca(vidas)).append("\n");
        sb.append("Palavra: ").append(palavraOculta).append("\n");
        sb.append("Letras tentadas: ").append(letrasUsadas).append("\n");
        sb.append("Vidas restantes: ").append(vidas).append("\n");
        sb.append(" VEZ DE: ").append(jogadorDaVez.toUpperCase()).append("\n");
        sb.append("------------------------------------");

        return sb.toString();
    }

    private static String desenharForca(int vidas) {
        switch (vidas) {
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
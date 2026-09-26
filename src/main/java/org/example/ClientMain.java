package org.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Digite o IP do servidor (aperte Enter para localhost): ");
            String host = scanner.nextLine().trim();
            if (host.isEmpty()) {
                host = "localhost";
            }

            System.out.print("Digite seu nome de usuário: ");
            String username = scanner.nextLine().trim();

            Registry registry = LocateRegistry.getRegistry(host, 1099);

            JogoForcaServerInterface jogoServer = (JogoForcaServerInterface) registry.lookup("JogoForcaService");

            JogoForcaClientImpl clientCallback = new JogoForcaClientImpl(username);

            jogoServer.registerClient(username, clientCallback);

            System.out.println("\n--- Conectado ao Jogo da Forca! (Digite 'sair' para encerrar) ---");
            System.out.print("> Digite um palpite (letra): ");

            while (true) {
                String message = scanner.nextLine();

                if ("sair".equalsIgnoreCase(message.trim())) {
                    jogoServer.unregisterClient(username);
                    System.out.println("Você saiu do jogo.");
                    System.exit(0);
                }

                if (!message.trim().isEmpty()) {
                    jogoServer.sendMessage(username, message);
                }
                System.out.print("> Digite um palpite (letra): ");
            }

        } catch (Exception e) {
            System.err.println("Erro no cliente do jogo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
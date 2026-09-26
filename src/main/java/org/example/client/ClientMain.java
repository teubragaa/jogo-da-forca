package org.example.client;

import org.example.rmi.JogoForcaClientImpl;
import org.example.rmi.JogoForcaServerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Digite o IP do servidor (Enter para localhost): ");
            String host = scanner.nextLine().trim();
            if (host.isEmpty()) host = "localhost";

            System.out.print("Digite seu nome: ");
            String username = scanner.nextLine().trim();

            Registry registry = LocateRegistry.getRegistry(host, 1099);
            JogoForcaServerInterface server = (JogoForcaServerInterface) registry.lookup("JogoForcaService");

            JogoForcaClientImpl client = new JogoForcaClientImpl();
            server.registerClient(username, client);

            while (true) {
                String input = scanner.nextLine();
                if ("sair".equalsIgnoreCase(input.trim())) {
                    server.unregisterClient(username);
                    System.exit(0);
                }
                if (!input.trim().isEmpty()) {
                    server.sendMessage(username, input);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
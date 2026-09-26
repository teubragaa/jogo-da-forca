package org.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Digite o IP do Servidor na LAN (ou aperte Enter para localhost): ");
            String hostIp = scanner.nextLine().trim();

            if (!hostIp.isEmpty()) {
                System.setProperty("java.rmi.server.hostname", hostIp);
            }

            int port = 1099;
            Registry registry = LocateRegistry.createRegistry(port);

            JogoForcaServerImpl server = new JogoForcaServerImpl();

            registry.rebind("JogoForcaService", server);

            System.out.println("==========================================");
            System.out.println("Servidor do Jogo da Forca RMI rodando na porta " + port);
            System.out.println("==========================================");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
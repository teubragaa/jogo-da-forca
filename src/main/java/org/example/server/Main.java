package org.example.server;

import org.example.rmi.JogoForcaServerImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite o IP do Servidor (Enter para localhost): ");
            String hostIp = scanner.nextLine().trim();

            if (!hostIp.isEmpty()) {
                System.setProperty("java.rmi.server.hostname", hostIp);
            }

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("JogoForcaService", new JogoForcaServerImpl());

            System.out.println("==========================================");
            System.out.println("Servidor RMI do Jogo da Forca Rodando!");
            System.out.println("==========================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
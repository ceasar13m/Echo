package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    Socket clientSocket;
    Scanner scanner;
    BufferedReader reader;
    BufferedWriter writer;

    public void run() {

        try {
            try {
                clientSocket = new Socket("localhost", 4005);

                System.out.println("Клиент запущен...");
                scanner = new Scanner(System.in);

                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


                String string = scanner.nextLine();


                while (!string.equals("exit")) {
                    System.out.println("New command from client: " + string);

                    writer.write(string + "\n");
                    writer.flush();

                    string = scanner.nextLine();
                }
            } finally {
                System.out.println("Клиент успешно закрыт...");
                clientSocket.close();
                reader.close();
                writer.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private Socket clientSocket;
    private ServerSocket serverSocket;


    public void run() {
        System.out.println("Запуск сервера...");

        try {
            try {
                serverSocket = new ServerSocket(8080);

                System.out.println("Сервер запущен...");

                while (true) {
                    System.out.println("Сервер ожидает нового клиента..");
                    clientSocket = serverSocket.accept();

                    System.out.println("Новый клиент..выделение потока..");
                    Worker serverWorker = new Worker(clientSocket);
                    serverWorker.start();
                }
            } finally {
                serverSocket.close();
                System.out.println("Сервер закрыт...");
            }
        } catch (IOException e) {
            System.err.println(e + "error");
        }

    }
}

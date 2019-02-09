package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    Socket clientSocket;
    ServerSocket serverSocket;


    public void run() {

        try {
            try {
                serverSocket = new ServerSocket(4005);

                System.out.println("Сервер запущен...");

                while(true) {
                    clientSocket = serverSocket.accept();
                    System.out.println("Новый клиент..выделение потока..");

                    Worker serverWorker = new Worker(clientSocket);
                    serverWorker.start();

                }
            } finally {
                serverSocket.close();
                System.out.println("Сервер закрыт...");
            }
        }
        catch (IOException e) {
            System.err.println(e + "error");
        }

    }
}

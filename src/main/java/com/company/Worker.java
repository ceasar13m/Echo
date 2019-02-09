package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker extends Thread {

    private Socket socket;
    BufferedWriter writer;
    BufferedReader reader;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Старт нового потока для клиента..");
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String message = null;
            message = reader.readLine();

            while (true) {

                if (message.equals("exit")) break;

                System.out.println("Сервер получил команду от клиента " + message);

                writer.write(message);
                writer.flush();

                message = reader.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                socket.close();
                reader.close();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}

package com.company;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        server.start();

        Client client = new Client();
        client.start();



        client.join();
        server.join();
    }
}

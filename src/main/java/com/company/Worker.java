package com.company;

import com.company.model.Good;
import com.company.model.Response;
import com.company.model.User;
import com.fasterxml.uuid.UUIDGenerator;
import com.fasterxml.uuid.UUIDType;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Worker extends Thread {

    private Socket socket;
    BufferedWriter writer;
    BufferedReader reader;
    private Gson gson;
    private UUID uuid;

    private InMemoryDB inMemoryDB;

    public Worker(Socket socket, InMemoryDB inMemoryDB) {
        this.socket = socket;
        this.inMemoryDB = inMemoryDB;
    }

    @Override
    public void run() {
        System.out.println("Старт нового потока для клиента..");
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            gson = new Gson();
            String message = null;
            message = reader.readLine();

            while (true) {
                System.out.println("Сервер получил команду от клиента " + message);

                if (message.equals("exit")) break;

                String command = parseCommand(message);
                if (command != null) {
                    switch (command) {
                        case "signup": {
                            processSignUp(message);
                            break;
                        }

                        case "signin": {
                            writer.write(processSignIn(message));
                            writer.flush();
                            break;
                        }

                        case "add": {
                            processAdd(message, writer);
                            break;
                        }

                        case "buy": {
                            break;
                        }

                        case "getall": {
                            break;
                        }

                        case "signout": {
                            break;
                        }

                        default: {
                            break;
                        }
                    }
                }

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


    /**
     * /signup/{"login":"dfd","password":"324"}
     * /signin/{"login":"dfd","password":"324"}
     * /add/...
     * /buy/...
     *
     * @param url
     * @return
     */
    private String parseCommand(String url) {
        String[] split = url.split("/");

        if (split.length < 2) return null;
        return split[1];

    }

    /**
     * Обращается в базу данных и проверяет логин и пароль.
     * Если есть такой пользователь возвращает какой-то код в зависимости....
     * Возвращает токен
     * @param url
     */
    private String processSignIn(String url) {
        String[] split = url.split("/");
        if(split.length < 3)
            return null;
        else  {
            User user = gson.fromJson(split[3], User.class);
            if(inMemoryDB.isLoginPasswordValid(user.login, user.password)) {
                UUID uuid = UUID.fromString(split[3]);
                inMemoryDB.addToken(uuid.toString());
                return uuid.toString();
            }
        }
        return  null;

    }

    /**
     * 1. Проверяет на корректность логина и пароля
     * 2. Не занят ли логин
     * 3. Проводит регистрацию и возвращает токен
     * @param url
     */
    private void processSignUp(String url) {

    }

    private void processAdd(String url, BufferedWriter writer) throws IOException {
        String[] ss = url.split("/");

        if(ss.length < 4) {
            Response response = new Response();
            response.code = 400;
            response.message = "Invalid arguments in the url";

            writer.write(gson.toJson(response, Response.class));
            writer.flush();
            return;
        }
        String token = ss[2];
        String jsonString = ss[3];

        if(inMemoryDB.isTokenValid(token)) {
            Good good = gson.fromJson(jsonString, Good.class);
            inMemoryDB.addGood(good);

            Response response = new Response();
            response.code = 200;
            response.message = "OK";
            writer.write(gson.toJson(response, Response.class));
            writer.flush();
        } else {
            Response response = new Response();
            response.code = 403;
            response.message = "Wrong token. Forbidden.";

            writer.write(gson.toJson(response, Response.class));
            writer.flush();
        }
    }

    private void processBuy(String url) {

    }

    private void processGetAll(String url) {

    }

    private void processSignOut(String url) {

    }
}

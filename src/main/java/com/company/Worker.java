package com.company;

import com.company.model.Good;
import com.company.model.Response;
import com.company.model.User;
import com.company.util.HttpStatus;
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
                System.out.println("Сервер получил команду от клиента: " + message);

                if (message.equals("exit")) break;

                String command = parseCommand(message);
                if (command != null) {
                    switch (command) {
                        case "signup": {
                            processSignUp(message);
                            break;
                        }

                        case "signin": {
                            processSignIn(message);
                            break;
                        }

                        case "add": {
                            processAdd(message, writer);
                            break;
                        }

                        case "buy": {
                            processBuy(message);
                            break;
                        }

                        case "getall": {
                            processGetAll();
                            break;
                        }

                        case "signout": {
                            processSignOut(message);
                            break;
                        }

                        default: {
                            break;
                        }
                    }
                }

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
     * 1. Проверяет на корректность логина и пароля
     * 2. Не занят ли логин
     * 3. Проводит регистрацию и возвращает токен
     *
     * @param url
     */
    private void processSignUp(String url) throws IOException {
        String[] split = url.split("/");
        if (split.length < 3)
            responseInvalidArgument(writer);
        else {
            User user = gson.fromJson(split[2], User.class);
            if (!inMemoryDB.isUserExists(user.login, user.password)) {
                // все ок, вернуть токен
                System.out.println("######## => Проводим регистрацию");
                UUID uuid = UUID.randomUUID();
                inMemoryDB.addToken(uuid.toString());
                inMemoryDB.addUser(user);

                Response response = createResponse(HttpStatus.OK, uuid.toString());
                String stringResponse = gson.toJson(response, Response.class);
                stringResponse += "\n";
                writer.write(stringResponse);
                writer.flush();

            } else {
                Response response = createResponse(HttpStatus.FORBIDDEN, "User already exists");
                String stringResponse = gson.toJson(response, Response.class);
                writer.write(stringResponse + "\n");
                writer.flush();
            }
        }
    }


    /**
     * Обращается в базу данных и проверяет логин и пароль.
     * Если есть такой пользователь возвращает какой-то код в зависимости....
     * Возвращает токен
     *
     * @param url
     */
    private void processSignIn(String url) throws IOException {
        String[] split = url.split("/");
        if (split.length < 3) {
            responseInvalidArgument(writer);
            return;
        } else {
            User user = gson.fromJson(split[2], User.class);
            if (inMemoryDB.isLoginPasswordValid(user.login, user.password)) {
                System.out.println("######## => Корректные данные входа");
                UUID uuid = UUID.randomUUID();
                inMemoryDB.addToken(uuid.toString());

                Response response = createResponse(HttpStatus.OK, uuid.toString());
                String stringResponse = gson.toJson(response, Response.class);
                stringResponse += "\n";
                writer.write(stringResponse);
                writer.flush();
            }
            else {
                Response response = createResponse(HttpStatus.BAD_REQUEST, "incorrect username or password");
                String stringResponse = gson.toJson(response, Response.class);
                stringResponse += "\n";
                writer.write(stringResponse);
                writer.flush();
            }
        }

    }


    private void processAdd(String url, BufferedWriter writer) throws IOException {
        String[] ss = url.split("/");

        if (ss.length < 4) {
            responseInvalidArgument(writer);
            return;
        }
        String token = ss[2];
        String jsonString = ss[3];

        if (inMemoryDB.isTokenValid(token)) {
            Good good = gson.fromJson(jsonString, Good.class);
            inMemoryDB.addGood(good);

            Response response = new Response();
            response.code = HttpStatus.OK;
            response.message = "OK";
            writer.write(gson.toJson(response, Response.class) + "\n");
            writer.flush();
        } else {
            writer.write(gson.toJson(createResponse(HttpStatus.FORBIDDEN, "Forbidden"), Response.class) + "\n");
            writer.flush();
        }
    }

    private void processBuy(String url) throws IOException {
        String[] ss = url.split("/");

        if (ss.length < 4) {
            responseInvalidArgument(writer);
            return;
        }
        String token = ss[2];
        String jsonString = ss[3];

        if (inMemoryDB.isTokenValid(token)) {
            Good good = gson.fromJson(jsonString, Good.class);
            if(inMemoryDB.buyGood(good)) {
                Response response = new Response();
                response.code = HttpStatus.OK;
                response.message = "OK";
                writer.write(gson.toJson(response, Response.class) + "\n");
                writer.flush();
            }else {
                Response response = new Response();
                response.code = HttpStatus.FORBIDDEN;
                response.message = "Unfortunately this item is not available at the moment";
                writer.write(gson.toJson(response, Response.class) + "\n");
                writer.flush();
            }

        } else {
            writer.write(gson.toJson(createResponse(HttpStatus.FORBIDDEN, "Forbidden"), Response.class) + "\n");
            writer.flush();
        }
    }

    private void processGetAll() throws IOException {
        writer.write(inMemoryDB.goodList()+ "\n");
        writer.flush();
    }

    private void processSignOut(String url) throws IOException {
        String[] split = url.split("/");
        if (split.length < 3) {
            responseInvalidArgument(writer);
            return;
        } else {
            if(inMemoryDB.isTokenValid(split[2])) {
                inMemoryDB.removeToken(split[2]);
                Response response = new Response();
                response.code = HttpStatus.OK;
                response.message = "OK";
                writer.write(gson.toJson(response, Response.class) + "\n");
                writer.flush();
            }
        }
    }

    private static Response createResponse(int code, String message) {
        Response response = new Response();
        response.code = code;
        response.message = message;

        return response;
    }

    private void responseInvalidArgument(BufferedWriter writer) throws IOException {
        writer.write(gson.toJson(createResponse(HttpStatus.BAD_REQUEST, "Invalid argument"), Response.class));
        writer.flush();
    }
}

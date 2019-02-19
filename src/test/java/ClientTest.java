
import com.company.Server;
import com.company.model.Good;
import com.company.model.Response;
import com.company.model.User;
import com.google.gson.Gson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientTest {


    @BeforeClass
    public static void startServer() {
        Server server = new Server();
        server.start();
    }

    @Test
    public void сlient1() {
        Gson gson = new Gson();


        try {
            Socket clientSocket = new Socket("localhost", 8080);
            Scanner scanner = new Scanner(System.in);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            User user = new User();
            //------------------------------------------------------------

            String signUpRequest = "/signup/";

            user.login = "ilnaz";
            user.password = "123";
            signUpRequest += gson.toJson(user, User.class);
            signUpRequest += "\n";
            writer.write(signUpRequest);
            writer.flush();

            // Проверка регистрации
            String signUpResponseString = reader.readLine();
            System.out.println("Получили от сервера после запроса signup: " + signUpResponseString);

            // ----------------------------------------------------------


            String signUpRequest2 = "/signup/";

            user.login = "ilnaz";
            user.password = "123";
            signUpRequest2 += gson.toJson(user, User.class);
            signUpRequest2 += "\n";
            writer.write(signUpRequest2);
            writer.flush();

            // Проверка регистрации
            String signUpResponseString2 = reader.readLine();
            System.out.println("Получили от сервера после запроса signup: " + signUpResponseString2);

            //------------------------------------------------------------


            String signInRequest = "/signin/";
            user.login = "ilnaz";
            user.password = "123";
            signInRequest += gson.toJson(user, User.class);
            signInRequest += "\n";
            writer.write(signInRequest);
            writer.flush();


            // Проверка входа
            // Ожидается токен
            String signInResponseString = reader.readLine();
            System.out.println("\n\nПолучили от сервера после запроса signin: " + signInResponseString + "\n");
            Response signInResponse = gson.fromJson(signInResponseString, Response.class);
            String token = signInResponse.message;
            System.out.println("Token: " + token);

            //-----------------------------------------------------------



            String addRequest = "/add/";
            Good good = new Good();
            good.name = "pizza";
            good.count = 4;
            addRequest += token + "/" + gson.toJson(good, Good.class) + "\n";
            writer.write(addRequest);
            writer.flush();

            String addResponseString = reader.readLine();
            System.out.println("От сервера получено после запроса add: " + addResponseString);
            Response addResponse = gson.fromJson(addResponseString, Response.class);

            //-----------------------------------------------------------------


            String addRequest2 = "/add/";
            Good good2 = new Good();
            good2.name = "pizza";
            good2.count = 5;
            addRequest2 += token + "/" + gson.toJson(good2, Good.class) + "\n";
            writer.write(addRequest2);
            writer.flush();

            String addResponseString2 = reader.readLine();
            System.out.println("От сервера получено после запроса add: " + addResponseString2);
            Response addResponse2 = gson.fromJson(addResponseString2, Response.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



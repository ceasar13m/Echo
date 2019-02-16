import com.company.Client;
import com.company.Server;
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

            user.login = "ainur";
            user.password = "123";
            signUpRequest += gson.toJson(user, User.class);
            signUpRequest += "\n";
            writer.write(signUpRequest);
            writer.flush();

            // Проверка регистрации
            String signUpResponseString = reader.readLine();
            System.out.println("Получили от сервера после запроса signup"+signUpResponseString);

            // ----------------------------------------------------------


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            String signInRequest = "/signin/";
            user.login = "ainur";
            user.password = "123";
            signInRequest += gson.toJson(user, User.class);
            signInRequest += "\n";
            writer.write(signInRequest);
            writer.flush();


            // Проверка входа
            // Ожидается токен
            String signInResponseString = reader.readLine();
            System.out.println("\n\nПолучили от сервера после запроса signin"+signInResponseString+"\n");
            Response signInResponse = gson.fromJson(signInResponseString, Response.class);
            String token = signInResponse.message;
            System.out.println("Token: "+token);

            //-----------------------------------------------------------




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import com.company.Client;
import com.company.Server;
import com.company.model.User;
import com.google.gson.Gson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
            Socket clientSocket = new Socket("localhost", 4005);
            Scanner scanner = new Scanner(System.in);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


            String signUpRequest = "/signup/";
            User user = new User();
            user.login = "ainur";
            user.password = "123";
            signUpRequest += gson.toJson(user, User.class);

            // Проверка регистрации
            writer.write(signUpRequest);
            writer.flush();

            String signUpResponse = scanner.nextLine();
            // .... реализовать проверку ответа

            // ----------------------------------------------------------

            String signInRequest = "/signin/";
            signInRequest += gson.toJson(user, User.class);
            writer.write(signInRequest);
            writer.flush();

            String signInResposnse = scanner.nextLine();
            // .... реализовать проверку ответа

            //-----------------------------------------------------------




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

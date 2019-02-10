package com.company;

import com.company.model.User;
import com.company.model.Users;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class UserWriter {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        String filename = "C:\\Users\\muhar\\IdeaProjects\\Echo\\src\\main\\java\\com\\company\\clientName.json";
        FileWriter fileWriter = new FileWriter(filename);

        Gson gson = new Gson();
        Users users = new Users();

        User user = new User();
        user.login = "ilnur";
        user.password = "332";

        users.adding(user);

        User user2 = new User();
        user2.login = "Ilnaz";
        user2.password = "123";

        users.adding(user2);



        fileWriter.write(gson.toJson(users.users));

        fileWriter.flush();
        fileWriter.close();

        UsersReader usersReader = new UsersReader();
        usersReader.printer();

    }
}

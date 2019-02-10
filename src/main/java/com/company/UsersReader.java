package com.company;

import com.company.model.User;
import com.company.model.Users;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class UsersReader {
    public void printer() throws FileNotFoundException {

        String filename = "C:\\Users\\muhar\\IdeaProjects\\Echo\\src\\main\\java\\com\\company\\clientName.json";
        FileReader fileReader = new FileReader(filename);

        Gson gson = new Gson();

        Users users = gson.fromJson(fileReader ,Users.class);

        for (User user : users.users) {
            System.out.println(user);
        }
    }
}

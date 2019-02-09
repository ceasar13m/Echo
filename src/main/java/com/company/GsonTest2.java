package com.company;

import com.company.model.Teacher;
import com.company.model.Teachers;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class GsonTest2 {
    public static void main(String[] args) throws FileNotFoundException {

        String filename = "C:\\Users\\muhar\\IdeaProjects\\Echo\\src\\main\\java\\com\\company\\clientName.json";
        FileReader fileReader = new FileReader(filename);

        Gson gson = new Gson();

        Teachers teachers = gson.fromJson(fileReader, Teachers.class);

        for (Teacher teacher : teachers.teachers) {
            System.out.println(teacher);
        }
    }
}

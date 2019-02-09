package com.company.model;

import java.util.ArrayList;

public class Teacher {


    public String name;

    public int exp;

    public ArrayList<String> subject;

    @Override
    public String toString() {

        String subj = "";
        for (int i = 0; i < subject.size(); i++) {
            subj = subj + ", " +subject.get(i);
        }

        return name + " " + exp + " " + subj;
    }

}

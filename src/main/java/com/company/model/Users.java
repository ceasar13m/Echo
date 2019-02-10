package com.company.model;

import java.util.ArrayList;
import java.util.List;

public class Users {

    public List<User> users;

    public Users() {
        users = new ArrayList<User>();
    }

    public void adding(User user) {
        users.add(user);
    }

}

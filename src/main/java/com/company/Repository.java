package com.company;

import com.company.model.Good;
import com.company.model.User;

public interface Repository {
    public boolean addUser(User user);
    public void addGood(Good good);
    public boolean buyGood(Good good);
    public String goodList();
    public boolean isLoginPasswordValid(String login, String password);
    public boolean isUserExists(String login, String password);
}

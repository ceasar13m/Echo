package com.company;

import com.company.model.Good;
import com.company.model.User;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDB {

    // Потокобезопасность
    private ConcurrentHashMap<String, String> authStorage = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> tokensStorage = new ConcurrentHashMap<>();

    //  Товары ищем по названию
    private ConcurrentHashMap<String, Integer> goodsStorage = new ConcurrentHashMap<>();

    private Object object = new Object();

    public boolean isUserExists(String login, String password) {
        if (authStorage.containsKey(login)) {
            return true;
        } else
            return false;
    }

    public boolean isLoginPasswordValid(String login, String password) {
        System.out.println(authStorage.containsKey("ainur"));
        if (authStorage.containsKey(login)) {
            if (authStorage.get(login).equals(password))
                return true;
            else
                return false;
        } else
            return false;
    }

    public boolean isTokenValid(String token) {
        return tokensStorage.containsKey(token);
    }

    public boolean addUser(User user) {
        if (authStorage.containsKey(user.login))
            return false;
        else {
            authStorage.put(user.login, user.password);
            return true;
        }
    }

    public boolean addToken(String token) {
        if (tokensStorage.containsKey(token)) return false;
        tokensStorage.put(token, object);
        return true;
    }

    public void removeToken(String token) {
        tokensStorage.remove(token);
    }

    public void addGood(Good good) {

    }
}

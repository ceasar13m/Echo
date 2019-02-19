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
        if (authStorage.containsKey(login)) {
            if (authStorage.get(login).equals(password))
                return true;
            else
                return false;
        } else
            return false;
    }

    public boolean isTokenValid(String token) {
        System.out.println(tokensStorage.containsKey(token));
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
        if (tokensStorage.containsKey(token))
            return false;
        tokensStorage.put(token, object);
        return true;
    }

    public void removeToken(String token) {
        tokensStorage.remove(token);
    }

    /**
     * Если в HashMap нет этого товара, добавляет
     * Если есть, то увеличивает количество
     * @param good
     */
    public void addGood(Good good) {
        if(!goodsStorage.containsKey(good.name)) {
            goodsStorage.put(good.name, good.count);
        }
        else {
            good.count += goodsStorage.get(good.name);
            goodsStorage.put(good.name, good.count);
        }
    }

    /**
     *
     * @param good
     * @return
     */
    public boolean buyGood(Good good) {
        if(goodsStorage.containsKey(good.name) && goodsStorage.get(good.name) >= good.count) {
            good.count -= goodsStorage.put(good.name, good.count);
            return true;
        }
        else {
            return false;
        }
    }
}

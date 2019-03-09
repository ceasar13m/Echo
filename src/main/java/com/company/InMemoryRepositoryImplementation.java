package com.company;

import com.company.model.Good;
import com.company.model.User;
import com.google.gson.Gson;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepositoryImplementation implements Repository{

    // Потокобезопасность
    private ConcurrentHashMap<String, String> authStorage = new ConcurrentHashMap<>();

    //  Товары ищем по названию
    private ConcurrentHashMap<String, Integer> goodsStorage = new ConcurrentHashMap<>();



    @Override
    public boolean isUserExists(String login, String password) {
        if (authStorage.containsKey(login)) {
            return true;
        } else
            return false;
    }


    @Override
    public boolean isLoginPasswordValid(String login, String password) {
        if (authStorage.containsKey(login)) {
            if (authStorage.get(login).equals(password))
                return true;
            else
                return false;
        } else
            return false;
    }







    @Override
    public boolean addUser(User user) {
        if (authStorage.containsKey(user.login))
            return false;
        else {
            authStorage.put(user.login, user.password);
            return true;
        }
    }

    /**
     * Если в HashMap нет этого товара, добавляет
     * Если есть, то увеличивает количество
     * @param good
     */

    @Override
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
    @Override
    public boolean buyGood(Good good) {
        if(goodsStorage.containsKey(good.name) && goodsStorage.get(good.name) >= good.count) {
            good.count -= goodsStorage.put(good.name, good.count);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String goodList() {
        Gson gson = new Gson();
        Good good = new Good();
        String jsonString = "";
        for (String string: goodsStorage.keySet()){
            String key = string.toString();
            int value = goodsStorage.get(key);
            good.name = key;
            good.count = value;

             jsonString += gson.toJson(good, Good.class);
        }
        return jsonString;
    }
}

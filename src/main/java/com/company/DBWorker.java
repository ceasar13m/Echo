package com.company;

import com.company.model.Good;
import com.company.model.User;
import com.google.gson.Gson;

import java.sql.*;

public class DBWorker {
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kazan13m";
    private static Connection connection;


    public DBWorker() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();

            statement.executeUpdate("create database IF NOT EXISTS DB;");
            statement.executeUpdate("use db");
            statement.executeUpdate("create table if not exists users (username varchar (30) not null, password varchar (30) not null);");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------

    public boolean isUserExists(String login, String password) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("use db");

        String tempString = "select * from users where username = '" + login + "'";
        ResultSet resultSet = statement.executeQuery(tempString);

        if(resultSet.next()) {
            return true;
        }
        else
            return false;
    }


    //------------------------------------------------------------------------------------------


    public boolean isLoginPasswordValid(String login, String password) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("use db");
        String tempString = "select * from users where username = '" + login + "'";
        ResultSet resultSet = statement.executeQuery(tempString);
        if(resultSet.next()) {
            if(resultSet.getString(1).equals(login) && resultSet.getString(2).equals(password))
                return true;
            else
                return false;
        }
        else
            return false;
    }


    //----------------------------------------------------------------------------------------

    public boolean addUser(User user) throws SQLException {

        Statement statement = connection.createStatement();

        statement.executeUpdate("use db");

        String tempString = "select * from users where username = '" + user.login + "'";
        ResultSet resultSet = statement.executeQuery(tempString);

        if(resultSet.next()) {
            return false;
        }
        else {
            String userInsertString = "insert into users (username, password) values ('" + user.login + "','" + user.password + "');";
            statement.executeUpdate(userInsertString);
            return true;
        }
    }


    //-----------------------------------------------------------------------------------------

    public void addGood(Good good) throws SQLException {

        Statement statement = connection.createStatement();

        statement.executeUpdate("use db");

        String tempString = "select * from goods where name = '" + good.name + "';";
        ResultSet resultSet = statement.executeQuery(tempString);

        if(!resultSet.next()) {
            String goodInsertString = "insert into goods (name, count) values ('" + good.name + "', " + good.count + ");";
            statement.executeUpdate(goodInsertString);
        }
        else {
            int sum = resultSet.getInt(2) + good.count;
            String goodInsertString = "update goods set count = " + sum + " where name = '" + good.name + "';";
            statement.executeUpdate(goodInsertString);
        }
    }

    //----------------------------------------------------------------------------------------

    public boolean buyGood(Good good) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("use db");

        String tempString = "select * from goods where name = '" + good.name + "'";
        ResultSet resultSet = statement.executeQuery(tempString);

        if(resultSet.next()) {
            if(resultSet.getInt(2) >= good.count) {
                int diff = resultSet.getInt(2) - good.count;
                String goodInsertString = "update goods set count = " + diff + " where name = '" + good.name + "';";
                statement.executeUpdate(goodInsertString);
                return true;
            }
            else
                return false;
        }
        else {
            return false;
        }
    }


    public String goodList() throws SQLException {
        Gson gson = new Gson();
        Good good = new Good();

        Statement statement = connection.createStatement();
        statement.executeUpdate("use db");
        String tempString = "select * from goods";
        ResultSet resultSet = statement.executeQuery(tempString);
        String jsonString = "";
        while(resultSet.next()) {
            good.name = resultSet.getString(1);
            good.count = resultSet.getInt(2);
            jsonString += gson.toJson(good, Good.class);
        }

        return jsonString;
    }
}

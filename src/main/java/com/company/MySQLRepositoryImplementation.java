package com.company;

import com.company.model.Good;
import com.company.model.User;
import com.google.gson.Gson;

import java.sql.*;

public class MySQLRepositoryImplementation implements Repository{
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kazan13m";
    private static Connection connection;


    public MySQLRepositoryImplementation() {
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

    @Override
    public boolean isUserExists(String login, String password) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("use db");

            String tempString = "select * from users where username = '" + login + "'";
            ResultSet resultSet = statement.executeQuery(tempString);

            if(resultSet.next()) {
                return true;
            }
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }


    //------------------------------------------------------------------------------------------


    @Override
    public boolean isLoginPasswordValid(String login, String password) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    //----------------------------------------------------------------------------------------------


    @Override
    public boolean addUser(User user){

        Statement statement;
        try {
            statement = connection.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    //-----------------------------------------------------------------------------------------

    @Override
    public void addGood(Good good) {

        Statement statement = null;
        try {
            statement = connection.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    //----------------------------------------------------------------------------------------

    @Override
    public boolean buyGood(Good good) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //------------------------------------------------------------------------------------------


    @Override
    public String goodList() {
        Gson gson = new Gson();
        Good good = new Good();

        Statement statement = null;
        try {
            statement = connection.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }
}

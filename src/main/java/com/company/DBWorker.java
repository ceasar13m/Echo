package com.company;

import com.company.model.User;

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
}

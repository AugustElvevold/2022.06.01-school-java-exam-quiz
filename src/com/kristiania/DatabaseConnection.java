package com.kristiania;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    Connection con;

    public DatabaseConnection() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"
                    + "quizDb"
                    + "?allowPublicKeyRetrieval=true&useSSL=false",
                    "User1",
                    "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

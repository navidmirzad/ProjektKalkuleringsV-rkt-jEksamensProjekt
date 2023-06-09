package com.example.projektkalkuleringsprojekt2semexam.repository.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseCon {

    // Metoden er static, da den hører til klassen selv og ikke en bestemt instans, af klassen.
    // Dette gør, at mange instanser af klassen kan have den samme connection,
    // og gør at vi ikke behøver at oprette en ny connection for hver instans.

    private static Connection con;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    @Value("${spring.datasource.url}")
    public void setUrl(String url) {
        URL = url;
    }

    @Value("${spring.datasource.username}")
    public void setUsername(String username) {
        USERNAME = username;
    }

    @Value("${spring.datasource.password}")
    public void setPassword(String password) {
        PASSWORD = password;
    }

    public static Connection getConnection() throws SQLException {
        try {
            if (con == null) {
                con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not connect to DB");
        }
        return con;
    }

}

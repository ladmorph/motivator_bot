package org.ladmorph.telegramBots.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static final Logger log = LoggerFactory.getLogger(DataBaseConnection.class);

    private static final String url = "jdbc:mysql://localhost:3306/telegram_bot?serverTimezone=UTC";
    private static final String username = "root";
    private static final String password = "1234";


    private DataBaseConnection() {
    }

    public static Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
            log.info("Connection was made successfully!");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return connection;
    }
}

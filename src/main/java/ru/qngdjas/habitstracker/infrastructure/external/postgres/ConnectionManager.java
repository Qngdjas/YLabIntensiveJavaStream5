package ru.qngdjas.habitstracker.infrastructure.external.postgres;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private static ConnectionManager instance;

    private String url;
    private String user;
    private String password;


    private ConnectionManager() {
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/postgres/config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Файл конфигурации не найден");
            }
            properties.load(inputStream);
            this.url = String.format("%s://%s:%s/%s", properties.getProperty("postgres.scheme"), properties.getProperty("postgres.host"), properties.getProperty("postgres.port"), properties.getProperty("postgres.db"));
            this.user = properties.getProperty("postgres.user");
            this.password = properties.getProperty("postgres.password");
            System.out.printf("%s\n%s\n%s", url, user, password);
        } catch (IOException exception) {
            System.out.printf("Не удалось подключиться к БД:\n%s\n", exception);
        }
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException exception) {
            throw new RuntimeException("Не удалось подключиться к БД:\n%s\n", exception);
        }
    }
}

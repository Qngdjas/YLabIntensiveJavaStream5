package ru.qngdjas.habitstracker.infrastructure.external.postgres;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationManager {

    public static void main(String[] args) {
//        TODO: Адаптировать к запуску с аргументом TEST
        migrate();
    }

    public static void migrate() {
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))) {
            Liquibase liquibase = new Liquibase("postgres/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены");
        } catch (SQLException | LiquibaseException exception) {
            System.out.printf("Ошибка миграции:\n%s\n", exception.getMessage());
            System.exit(0);
        }
    }
}

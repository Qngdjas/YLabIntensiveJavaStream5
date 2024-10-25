package ru.qngdjas;

import ru.qngdjas.habitstracker.Application;

import java.io.IOException;

/**
 * Главный класс приложения.
 * Представляет точку входа в программу.
 */
public class Main {

    /**
     * Метод запуска приложения.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        Application application = new Application();
        try {
            application.start();
        } catch (IOException exception) {
            System.out.printf("Ошибка запуска приложения: %s\n", exception.getMessage());
        }
    }
}
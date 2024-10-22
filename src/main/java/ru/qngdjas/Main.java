package ru.qngdjas;

import ru.qngdjas.habitstracker.Application;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Application application = new Application();
        try {
            application.start();
        } catch (IOException exception) {
            System.out.printf("Ошибка запуска приложения: %s\n", exception.getMessage());
        }
    }
}
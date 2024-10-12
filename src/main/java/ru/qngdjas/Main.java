package ru.qngdjas;

import ru.qngdjas.habitstracker.Application;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Application application = new Application();
        try {
            application.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
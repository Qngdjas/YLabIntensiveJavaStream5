package ru.qngdjas.habitstracker;

import ru.qngdjas.habitstracker.command.CommandManager;

import java.io.IOException;
import java.util.Scanner;

public class Application {

    private static final Scanner reader = new Scanner(System.in);
    private static final CommandManager commandManager = new CommandManager();

    public void start() throws IOException {
        System.out.println("Добро пожаловать в планировщик привычек. Список команд доступен через help.");
        String input;
        while (true) {
            input = reader.nextLine();
            commandManager.execute(input);
        }
    }
}

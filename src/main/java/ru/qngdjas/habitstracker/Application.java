package ru.qngdjas.habitstracker;

import ru.qngdjas.habitstracker.api.command.CommandManager;

import java.io.IOException;
import java.util.Scanner;

/**
 * Класс приложения управления привычками.
 */
public class Application {

    /**
     * Объект чтения пользовательского ввода.
     */
    private static final Scanner reader = new Scanner(System.in);
    /**
     * Менеджер команд для выполнения действий, запрошенных пользователем.
     */
    private static final CommandManager commandManager = new CommandManager();

    /**
     * Метод запуска приложения.
     * <p>Выводит приветсвенное сообщение и ожидает ввода команд.
     * Команды обрабатываются {@link CommandManager}.
     *
     * @throws IOException Непредвиденная ошибка ввода-вывода.
     */
    public void start() throws IOException {
        System.out.println("Добро пожаловать в планировщик привычек. Список команд доступен через help.");
        String input;
        while (true) {
            input = reader.nextLine();
            commandManager.execute(input);
        }
    }
}

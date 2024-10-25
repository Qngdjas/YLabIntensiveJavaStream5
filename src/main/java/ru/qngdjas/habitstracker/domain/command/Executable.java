package ru.qngdjas.habitstracker.domain.command;

/**
 * Интерфейс консольных команд.
 * <p>Команды должны реализовать интерфейс выполения действий {@link #execute()} в соответствии с собственным назначением.
 */
public interface Executable {

    /**
     * Метод выполнения логики команды.
     */
    void execute();
}

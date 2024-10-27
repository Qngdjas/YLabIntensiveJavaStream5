package ru.qngdjas.habitstracker.api.command;

import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.service.HabitService;
import ru.qngdjas.habitstracker.domain.service.UserService;
import ru.qngdjas.habitstracker.infrastructure.session.Session;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс менеджера консольных команд.
 */
public class CommandManager {

    /**
     * Перечисление доступных команд.
     * <p>Представляет реализацию интерфейса {@link Executable}.
     */
    enum Command implements Executable {
        /**
         * Коамнда, предоставляющая список доступных команд.
         */
        help {
            @Override
            public void execute() {
                System.out.printf("Список доступных команд:\n%s\n", Arrays.toString(Command.values()));
            }
        },
        /**
         * Команда, предоставляющая информацию о текущей сессии.
         */
        session {
            @Override
            public void execute() {
                System.out.println("Информация о сессии:");
                System.out.printf("Текущий пользователь %s\n", Session.getInstance().getUser());
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда регистрации в приложении.
         */
        register {
            @Override
            public void execute() {
                System.out.println("Регистрация пользователя:");
                System.out.println("Введите email");
                String email = reader.nextLine();
                System.out.println("Введите пароль");
                String password = reader.nextLine();
                System.out.println("Введите имя");
                String name = reader.nextLine();
                User user = userService.register(email, password, name, false);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда аутентификации в приложении.
         */
        login {
            @Override
            public void execute() {
                System.out.println("Авторизация пользователя:");
                System.out.println("Введите email");
                String email = reader.nextLine();
                System.out.println("Введите пароль");
                String password = reader.nextLine();
                User user = userService.login(email, password);
                System.out.println("Выполнено");
            }

        },
        /**
         * Команда обновления собственных учетных данных пользователя.
         */
        update_user {
            @Override
            public void execute() {
                System.out.println("Обновление данных пользователя:");
                System.out.println("Введите email");
                String email = reader.nextLine();
                System.out.println("Введите пароль");
                String password = reader.nextLine();
                System.out.println("Введите имя");
                String name = reader.nextLine();
                User user = userService.update(email, password, name);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда удаления пользователя.
         * <p>В соответствии с {@link UserService#delete(String)} пользователю
         * предоставляется возможность удалить собственную учетную запись,
         * администратор может удалять любые учетные записи.
         */
        delete_user {
            @Override
            public void execute() {
                System.out.println("Удаление пользователя:");
                System.out.println("Введите email");
                String email = reader.nextLine();
                User user = userService.delete(email);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда добавления новой привычки.
         * <p>{@link HabitService} ограничивает операции только для собственных привычек.
         */
        add_habit {
            @Override
            public void execute() {
                System.out.println("Добавление привычки:");
                System.out.println("Введите наименование");
                String name = reader.nextLine();
                System.out.println("Введите описание");
                String description = reader.nextLine();
                System.out.println("Привычка ежедневная (д/н)");
                boolean isDaily = OK.contains(reader.nextLine());
                Habit habit = habitService.add(name, description, isDaily);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда обновления привычки.
         * <p>{@link HabitService} ограничивает операции только для собственных привычек.
         */
        update_habit {
            @Override
            public void execute() {
                System.out.println("Обновление привычки:");
                System.out.println("Ведите наименование привычки, которую вы хотите обновить");
                String currentHabitName = reader.nextLine();
                System.out.println("Введите наименование");
                String habitName = reader.nextLine();
                System.out.println("Введите описание");
                String description = reader.nextLine();
                System.out.println("Привычка ежедневная (д/н)");
                boolean isDaily = OK.contains(reader.nextLine());
                Habit habit = habitService.update(currentHabitName, habitName, description, isDaily);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда получения списка привычек пользователя.
         * <p>{@link HabitService} ограничивает операции только для собственных привычек.
         */
        get_habits {
            @Override
            public void execute() {
                System.out.println("Получение привычек:");
                List<Habit> habits = habitService.getAll();
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда удаления привычек.
         * <p>{@link HabitService} ограничивает операции только для собственных привычек.
         */
        delete_habit {
            @Override
            public void execute() {
                System.out.println("Удаление привычки:");
                System.out.println("Введите привычку");
                String habitName = reader.nextLine();
                Habit habit = habitService.delete(habitName);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда отметки выполнения привычек.
         * <p>{@link HabitService} ограничивает операции только для собственных привычек.
         */
        note_habit {
            @Override
            public void execute() {
                System.out.println("Отметить привычку:");
                System.out.println("Введите наименование привычки");
                String habitName = reader.nextLine();
                System.out.println("Введите дату в формате \"yyyy-MM-dd\" (оставьте поле пустым, чтобы отметить на сегодня)");
                String date = reader.nextLine();
                LocalDate note = habitService.note(habitName, date);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда получения текущей серии выполнения привычек.
         * <p>{@link HabitService} ограничивает операции только для собственных привычек.
         */
        get_streak {
            @Override
            public void execute() {
                System.out.println("Вывод текущих серий выполнения привычек:");
                Map<String, Long> habitStreak = habitService.getStreak();
                System.out.println(habitStreak.toString());
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда получения успеваемости по привычке за период.
         * <p>{@link HabitService} ограничивает операции только для собственных привычек.
         */
        get_hit {
            @Override
            public void execute() {
                System.out.println("Вывод процента успеха выполнения привычки:");
                System.out.println("Введите наименование привычки");
                String habitName = reader.nextLine();
                System.out.println("Введите дату начала периода в формате \"yyyy-MM-dd\" (оставьте поле пустым, чтобы отметить на сегодня)");
                String beginDate = reader.nextLine();
                System.out.println("Введите дату конца периода в формате \"yyyy-MM-dd\" (оставьте поле пустым, чтобы отметить на сегодня)");
                String endDate = reader.nextLine();
                double hit = habitService.getHit(habitName, beginDate, endDate);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда предоставления отчета успеваемости по всем привычкам.
         * <p>{@link HabitService} ограничивает операции только для собственных привычек.
         */
        get_hits {
            @Override
            public void execute() {
                System.out.println("Вывод прогресса по всем привычкам:");
                System.out.println("Введите дату начала периода в формате \"yyyy-MM-dd\" (оставьте поле пустым, чтобы отметить на сегодня)");
                String beginDate = reader.nextLine();
                System.out.println("Введите дату конца периода в формате \"yyyy-MM-dd\" (оставьте поле пустым, чтобы отметить на сегодня)");
                String endDate = reader.nextLine();
                Map<Habit, Double> hits = habitService.getHits(beginDate, endDate);
                System.out.println("Выполнено");
            }
        },
        /**
         * Команда завершения работы приложения.
         */
        exit {
            @Override
            public void execute() {
                System.out.println("Заверешение работы");
                System.exit(0);
            }
        };

        /**
         * Объект чтения пользовательского ввода.
         */
        private static final Scanner reader = new Scanner(System.in);
        /**
         * Сервис обработки запросов управления пользователями.
         */
        private static final UserService userService = new UserService();
        /**
         * Сервис обработки запросов управления привычками.
         */
        private static final HabitService habitService = new HabitService();
        /**
         * Список распознаваемых командами положительных ответов.
         */
        private static final Set<String> OK = new HashSet<>();

        static {
            OK.add("+");
            OK.add("д");
            OK.add("да");
            OK.add("y");
            OK.add("yes");
        }

    }

    /**
     * Обработчик команд.
     * <p>Передает выполнение команды в {@link Command} если она существует.
     *
     * @param input Ожидаемая команда.
     */
    public void execute(String input) {
        try {
            Executable command = Command.valueOf(input.toLowerCase());
            command.execute();
        } catch (IllegalArgumentException exception) {
            System.out.printf("Команды %s не существует.\n", input);
        }
    }
}

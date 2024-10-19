package ru.qngdjas.habitstracker.domain.command;

import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.infrastructure.service.HabitService;
import ru.qngdjas.habitstracker.infrastructure.service.UserService;
import ru.qngdjas.habitstracker.infrastructure.session.Session;

import java.time.LocalDate;
import java.util.*;

public class CommandManager {

    enum Command implements Executable {
        help {
            @Override
            public void execute() {
                System.out.printf("Список доступных команд:\n%s\n", Arrays.toString(Command.values()));
            }
        },
        session {
            @Override
            public void execute() {
                System.out.println("Информация о сессии:");
                System.out.printf("Текущий пользователь %s\n", Session.getInstance().getUser());
                System.out.println("Выполнено");
            }
        },
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
                User user = userService.register(email, password, name);
                System.out.println("Выполнено");
            }
        },
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
        update_habit {
            @Override
            public void execute() {
                System.out.println("Обновление привычки:");
                System.out.println("Ведите наименование привычки, которую вы хотите обновить");
                String oldHabitName = reader.nextLine();
                System.out.println("Введите наименование");
                String habitName = reader.nextLine();
                System.out.println("Введите описание");
                String description = reader.nextLine();
                System.out.println("Привычка ежедневная (д/н)");
                boolean isDaily = OK.contains(reader.nextLine());
                Habit habit = habitService.update(oldHabitName, habitName, description, isDaily);
                System.out.println("Выполнено");
            }
        },
        get_habits {
            @Override
            public void execute() {
                System.out.println("Получение привычек:");
                List<Habit> habits = habitService.getAll();
                System.out.println("Выполнено");
            }
        },
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
        get_streak {
            @Override
            public void execute() {
                System.out.println("Вывод текущих серий выполнения привычек:");
                Map<Habit, Long> habitStreak = habitService.getStreak();
                System.out.println(habitStreak.toString());
                System.out.println("Выполнено");
            }
        },
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
        exit {
            @Override
            public void execute() {
                System.out.println("Заверешение работы");
                System.exit(0);
            }
        };

        private static final Scanner reader = new Scanner(System.in);
        private static final Set<String> OK = new HashSet<>();

        static {
            OK.add("+");
            OK.add("д");
            OK.add("да");
            OK.add("y");
            OK.add("yes");
        }

        private static final UserService userService = new UserService();
        private static final HabitService habitService = new HabitService();
    }

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        for (Command command : Command.values()) {
            commands.put(command.name(), command);
        }
    }

    public void execute(String input) {
        Command command;
        try {
            command = commands.get(input);
            command.execute();
        } catch (NullPointerException exception) {
            System.out.printf("Команды %s не существует.%n", input);
        }
    }
}

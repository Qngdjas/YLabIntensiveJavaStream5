package ru.qngdjas.habitstracker.domain.service;

import ru.qngdjas.habitstracker.application.dto.habit.HabitCreateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.application.mapper.model.HabitMapper;
import ru.qngdjas.habitstracker.application.utils.validator.HabitValidator;
import ru.qngdjas.habitstracker.application.utils.validator.ValidationException;
import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.IHabitRepository;
import ru.qngdjas.habitstracker.domain.repository.IHabitNotesRepository;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;
import ru.qngdjas.habitstracker.domain.service.core.RootlessException;
import ru.qngdjas.habitstracker.domain.service.core.Service;
import ru.qngdjas.habitstracker.infrastructure.persistance.HabitRepository;
import ru.qngdjas.habitstracker.infrastructure.persistance.HabitNotesRepository;
import ru.qngdjas.habitstracker.infrastructure.session.Session;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Сервис обработки запросов управления привычками.
 */
public class HabitService extends Service {

    /**
     * Репозитории CRUD-операций над моделями привычек.
     */
    private static final IHabitRepository habitRepository = new HabitRepository();
    private static final IHabitNotesRepository statisticRepository = new HabitNotesRepository();
    private static final HabitMapper mapper = HabitMapper.INSTANCE;

    /**
     * Метод добавления привычки.
     *
     * @param habitName   Наименование привычки.
     * @param description Описание привычки.
     * @param isDaily     Тип привычки (ежедневная/еженедельная).
     * @return Новая привычка {@link Habit}, если входные данные корректны,
     * <p>иначе {@code null}.
     */
    public Habit add(String habitName, String description, boolean isDaily) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            if (!habitRepository.isExists(user.getId(), habitName)) {
                Habit habit = new Habit(-1, habitName, description, isDaily, LocalDate.now(), user.getId());
                habitRepository.create(habit);
                return habit;
            }
            System.out.printf("Привычка %s у пользователя %s уже существует", habitName, user.getEmail());
        }
        return null;
    }

    public Habit add(HabitCreateDTO habitDTO) throws ValidationException, AlreadyExistsException {
        HabitValidator.validate(habitDTO);
        if (habitRepository.isExists(habitDTO.getUserId(), habitDTO.getName())) {
            throw new AlreadyExistsException(String.format("Привычка %s у пользователя уже существует", habitDTO.getName()));
        }
        return habitRepository.create(mapper.toHabit(habitDTO));
    }

    /**
     * Метод обновление привычки.
     *
     * @param currentHabitName Текущее наименование привычки.
     * @param habitName        Наименование привычки.
     * @param description      Описание привычки.
     * @param isDaily          Тип привычки (ежедневная/еженедельная).
     * @return Обновленная привычка {@link Habit}, если входные данные корректны,
     * <p>иначе {@code null}.
     */
    public Habit update(String currentHabitName, String habitName, String description, boolean isDaily) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            if (!habitRepository.isExists(user.getId(), habitName)) {
                Habit habit = habitRepository.retrieveByUserIDAndName(user.getId(), currentHabitName);
                if (habit != null) {
                    habit.setName(habitName);
                    habit.setDescription(description);
                    habit.setDaily(isDaily);
                    habitRepository.update(habit);
                    System.out.printf("Привычка %s обновлена\n", habit.getName());
                    return habit;
                }
                System.out.printf("Привычка %s у пользователя %s не найдена", currentHabitName, user.getEmail());
            } else {
                System.out.printf("Привычка %s уже существует", habitName);
            }
        }
        return null;
    }

    public Habit update(HabitDTO habitDTO) throws ValidationException, AlreadyExistsException, RootlessException {
        HabitValidator.validate(habitDTO);
        if (habitRepository.isExists(habitDTO.getUserId(), habitDTO.getName())) {
            throw new AlreadyExistsException(String.format("Привычка %s уже существует", habitDTO.getName()));
        }
//        Habit habit = habitRepository.retrieveByUserIDAndID(habitDTO.getUserId(), habitDTO.getName());
//        if (id == habitDTO.getUserId()) {
        return habitRepository.update(mapper.toHabit(habitDTO));
//        }
//        throw new RootlessException();
    }

    /**
     * Метод удаления привычки.
     *
     * @param habitName Наименование привычки.
     * @return Удаленная привычка {@link Habit}, если входные данные корректны,
     * <p>иначе {@code null}.
     */
    public Habit delete(String habitName) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            Habit habit = habitRepository.retrieveByUserIDAndName(user.getId(), habitName);
            if (habit != null) {
                habitRepository.delete(habit.getId());
                System.out.printf("Привычка %s у пользователя %s удалена\n", habitName, user.getEmail());
                return habit;
            }
            System.out.printf("Привычка %s у пользователя %s не найдена", habitName, user.getEmail());
        }
        return null;
    }

    /**
     * Метод получения привычки.
     *
     * @param habitName Наименование привычки.
     * @return Привычка {@link Habit}, если найдена,
     * <p>иначе {@code null}.
     */
    public Habit get(String habitName) {
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            Habit habit = habitRepository.retrieveByUserIDAndName(user.getId(), habitName);
            if (habit != null) {
                System.out.println(habit);
                return habit;
            }
            System.out.printf("Привычка %s у пользователя %s не найдена", habitName, user.getEmail());
        }
        return null;
    }

    /**
     * Метод получения всех привычек пользователя.
     *
     * @return Список привычек {@link List}{@code <}{@link Habit}{@code >},
     * <p>если у пользователя нет привычек, выводит информационное сообщение в консоль.
     */
    public List<Habit> getAll() {
        List<Habit> habits = new ArrayList<>();
        if (isAuth()) {
            User user = Session.getInstance().getUser();
            habits = habitRepository.listByUserID(user.getId());
            if (habits.isEmpty()) {
                System.out.printf("Привычки у пользователя %s не найдены", user.getEmail());
            } else {
                System.out.printf("Привычки пользователя:\n%s\n", habits);
            }
        }
        return habits;
    }

    /**
     * Метод отметки выполнения привычки на определенную дату.
     * <p>Если дата не задана явно, используются текущие сутки.
     *
     * @param habitName Наименование привычки.
     * @param date      Дата отметки.
     * @return Дата отметки.
     */
    public LocalDate note(String habitName, String date) {
        if (isAuth()) {
            try {
                Habit habit = get(habitName);
                if (habit != null) {
                    LocalDate noteDate = date.isBlank() ? LocalDate.now() : LocalDate.parse(date);
                    return statisticRepository.note(habit.getId(), noteDate);
                }
            } catch (DateTimeParseException exception) {
                System.out.println("Неверный формат даты");
            }
        }
        return null;
    }

    /**
     * Метод получения текущей серии выполнения привычек.
     *
     * @return Словарь привычек с указанием текущей серии.
     */
    public Map<String, Long> getStreak() {
        Map<String, Long> streaks = new HashMap<>();
        if (isAuth()) {
            List<Habit> habits = getAll();
            for (Habit habit : habits) {
                streaks.put(habit.getName(), statisticRepository.getStreak(habit.getId()));
            }
        }
        return streaks;
    }

    /**
     * Метод получения успеваемости по конкретной привычке за период.
     * <p>Если дата не задана явно, используются текущие сутки.
     *
     * @param habitName Наименование привычки.
     * @param beginDate Дата начала отчетного периода.
     * @param endDate   Дата завершения отчетного периода.
     * @return Процент успеваемости по привычке.
     */
    public double getHit(String habitName, String beginDate, String endDate) {
        double result = 0.0f;
        if (isAuth()) {
            try {
                Habit habit = get(habitName);
                if (habit != null) {
                    LocalDate noteBeginDate = beginDate.isBlank() ? LocalDate.now() : LocalDate.parse(beginDate);
                    LocalDate noteEndDate = beginDate.isBlank() ? LocalDate.now() : LocalDate.parse(endDate);
                    System.out.printf("Процент успеха по привычке %s:\n", habit.getName());
                    result = statisticRepository.getHit(habit.getId(), noteBeginDate, noteEndDate);
                }
            } catch (DateTimeParseException exception) {
                System.out.println("Неверный формат даты");
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return result;
    }

    /**
     * Метод получения успеваемости по всем привычкам пользователя за период.
     * <p>Если дата не задана явно, используются текущие сутки.
     *
     * @param beginDate Дата начала отчетного периода.
     * @param endDate   Дата завершения отчетного периода.
     * @return Словарь привычек с указанием успеваемости.
     */
    public Map<Habit, Double> getHits(String beginDate, String endDate) {
        Map<Habit, Double> result = new HashMap<>();
        if (isAuth()) {
            try {
                List<Habit> habits = getAll();
                LocalDate noteBeginDate = beginDate.isBlank() ? LocalDate.now() : LocalDate.parse(beginDate);
                LocalDate noteEndDate = beginDate.isBlank() ? LocalDate.now() : LocalDate.parse(endDate);
                for (Habit habit : habits) {
                    result.put(habit, statisticRepository.getHit(habit.getId(), noteBeginDate, noteEndDate));
                }
                System.out.printf("Отчёт по привычкам:\n%s\n", result);
                return result;
            } catch (DateTimeParseException exception) {
                System.out.println("Неверный формат даты");
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return result;
    }
}

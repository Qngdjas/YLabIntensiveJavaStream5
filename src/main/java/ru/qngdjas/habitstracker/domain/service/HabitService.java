package ru.qngdjas.habitstracker.domain.service;

import ru.qngdjas.habitstracker.application.dto.habit.HabitCreateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.application.dto.habit.NotedDateDTO;
import ru.qngdjas.habitstracker.application.mapper.model.HabitMapper;
import ru.qngdjas.habitstracker.application.utils.validator.HabitValidator;
import ru.qngdjas.habitstracker.application.utils.validator.ValidationException;
import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.model.user.User;
import ru.qngdjas.habitstracker.domain.repository.IHabitRepository;
import ru.qngdjas.habitstracker.domain.repository.IHabitNotesRepository;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;
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
     * @param habitDTO Данные привычки.
     * @return Новая привычка {@link Habit}, если входные данные корректны,
     * <p>иначе {@code null}.
     */
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
     * @param habitDTO Обновленные данные привычки.
     * @return Обновленная привычка {@link Habit}, если входные данные корректны,
     * <p>иначе {@code null}.
     */
    public Habit update(HabitDTO habitDTO) throws ValidationException, NotFoundException, AlreadyExistsException, RootlessException {
        HabitValidator.validate(habitDTO);
        Habit habit = habitRepository.retrieve(habitDTO.getId());
        if (habit == null) {
            throw new NotFoundException(String.format("Привычки с id=%d не существует", habitDTO.getId()));
        }
        if (!habit.getName().equals(habitDTO.getName()) && habitRepository.isExists(habitDTO.getUserId(), habitDTO.getName())) {
            throw new AlreadyExistsException(String.format("Привычка %s уже существует", habitDTO.getName()));
        }
        if (habit.getUserID() == habitDTO.getUserId()) {
            return habitRepository.update(mapper.toHabit(habitDTO));
        }
        throw new RootlessException();
    }

    /**
     * Метод получения привычки.
     *
     * @param userId Идентификатор пользователя, выполняющего операцию.
     * @param id     Идентификатор привычки.
     * @return Привычка {@link Habit}, если найдена,
     * <p>иначе {@code null}.
     */
    public Habit get(long userId, long id) throws NotFoundException, RootlessException {
        Habit habit = habitRepository.retrieve(id);
        if (habit == null) {
            throw new NotFoundException(String.format("Привычки с id=%d не существует", id));
        }
        if (userId == habit.getUserID()) {
            return habit;
        }
        throw new RootlessException();
    }

    /**
     * Метод получения всех привычек пользователя.
     *
     * @param userId Идентификатор пользователя, выполняющего операцию.
     * @return Список привычек {@link List}{@code <}{@link Habit}{@code >},
     * <p>если у пользователя нет привычек, выводит информационное сообщение в консоль.
     */
    public List<Habit> getAll(long userId) throws NotFoundException {
        List<Habit> habits = habitRepository.listByUserID(userId);
        if (habits.isEmpty()) {
            throw new NotFoundException("Привычки не найдены");
        }
        return habits;
    }

    /**
     * Метод удаления привычки.
     *
     * @param userId Идентификатор пользователя, выполняющего операцию.
     * @param id     Идентификатор привычки.
     * @return Удаленная привычка {@link Habit}, если входные данные корректны,
     * <p>иначе {@code null}.
     */
    public Habit delete(long userId, long id) throws NotFoundException, RootlessException {
        Habit habit = get(userId, id);
        habitRepository.delete(id);
        return habit;
    }

    /**
     * Метод отметки выполнения привычки на определенную дату.
     * <p>Если дата не задана явно, используются текущие сутки.
     *
     * @param userId       Идентификатор пользователя, выполняющего операцию.
     * @param id           Идентификатор привычки.
     * @param notedDateDTO Дата отметки.
     * @return Дата отметки.
     */
    public LocalDate note(long userId, long id, NotedDateDTO notedDateDTO) throws RootlessException, NotFoundException, DateTimeParseException {
        Habit habit = get(userId, id);
        LocalDate noteDate = notedDateDTO.getDate() == null || notedDateDTO.getDate().isBlank() ? LocalDate.now() : LocalDate.parse(notedDateDTO.getDate());
        return statisticRepository.note(habit.getId(), noteDate);
    }

    /**
     * Метод получения текущей серии выполнения привычек.
     *
     * @param userId Идентификатор пользователя, выполняющего операцию.
     * @return Словарь привычек с указанием текущей серии.
     */
    public Map<String, String> getStreak(long userId) {
        Map<String, String> streaks = new HashMap<>();
        List<Habit> habits = getAll(userId);
        for (Habit habit : habits) {
            streaks.put(habit.getName(), statisticRepository.getStreak(habit.getId()));
        }
        return streaks;
    }

    /**
     * Метод получения успеваемости по конкретной привычке за период.
     * <p>Если дата не задана явно, используются текущие сутки.
     *
     * @param userId    Идентификатор пользователя, выполняющего операцию.
     * @param id        Идентификатор привычки.
     * @param beginDate Дата начала отчетного периода.
     * @param endDate   Дата завершения отчетного периода.
     * @return Процент успеваемости по привычке.
     */
    public double getHit(long userId, long id, String beginDate, String endDate) {
        double result = 0.0f;
        try {
            Habit habit = get(userId, id);
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
        try {
            List<Habit> habits = new ArrayList<>();//getAll();
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
        return result;
    }
}

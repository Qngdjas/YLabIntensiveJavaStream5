package ru.qngdjas.habitstracker.domain.service;

import org.springframework.stereotype.Service;
import ru.qngdjas.habitstracker.application.dto.habit.HabitCreateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.application.dto.habit.NotedDateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.NotedPeriodDTO;
import ru.qngdjas.habitstracker.application.mapper.model.HabitMapper;
import ru.qngdjas.habitstracker.application.utils.logger.ExecutionLoggable;
import ru.qngdjas.habitstracker.application.utils.validator.HabitValidator;
import ru.qngdjas.habitstracker.application.utils.validator.ValidationException;
import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.repository.IHabitRepository;
import ru.qngdjas.habitstracker.domain.repository.IHabitNotesRepository;
import ru.qngdjas.habitstracker.domain.service.core.AlreadyExistsException;
import ru.qngdjas.habitstracker.domain.service.core.NotFoundException;
import ru.qngdjas.habitstracker.domain.service.core.RootlessException;
import ru.qngdjas.habitstracker.infrastructure.persistance.HabitRepository;
import ru.qngdjas.habitstracker.infrastructure.persistance.HabitNotesRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Сервис обработки запросов управления привычками.
 */
@ExecutionLoggable
@Service
public class HabitService {

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
        if (habit.getUserId() == habitDTO.getUserId()) {
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
        if (userId == habit.getUserId()) {
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
    public Map<String, String> getStreak(long userId) throws NotFoundException {
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
     * @param userId         Идентификатор пользователя, выполняющего операцию.
     * @param id             Идентификатор привычки.
     * @param notedPeriodDTO Данные отчетного периода.
     * @return Процент успеваемости по привычке.
     */
    public String getHit(long userId, long id, NotedPeriodDTO notedPeriodDTO) throws RootlessException, NotFoundException, DateTimeParseException, IllegalArgumentException {
        Habit habit = get(userId, id);
        LocalDate noteBeginDate = notedPeriodDTO.getBeginDate() == null || notedPeriodDTO.getBeginDate().isBlank() ?
                LocalDate.now() :
                LocalDate.parse(notedPeriodDTO.getBeginDate());
        LocalDate noteEndDate = notedPeriodDTO.getEndDate() == null || notedPeriodDTO.getEndDate().isBlank() ?
                LocalDate.now() :
                LocalDate.parse(notedPeriodDTO.getEndDate());
        return statisticRepository.getHit(habit.getId(), noteBeginDate, noteEndDate);
    }

    /**
     * Метод получения успеваемости по всем привычкам пользователя за период.
     * <p>Если дата не задана явно, используются текущие сутки.
     *
     * @param userId         Идентификатор пользователя, выполняющего операцию.
     * @param notedPeriodDTO Данные отчетного периода.
     * @return Словарь привычек с указанием успеваемости.
     */
    public Map<String, String> getHits(long userId, NotedPeriodDTO notedPeriodDTO) throws RootlessException, NotFoundException, DateTimeParseException, IllegalArgumentException {
        Map<String, String> result = new HashMap<>();
        List<Habit> habits = getAll(userId);
        LocalDate noteBeginDate = notedPeriodDTO.getBeginDate() == null || notedPeriodDTO.getBeginDate().isBlank() ?
                LocalDate.now() :
                LocalDate.parse(notedPeriodDTO.getBeginDate());
        LocalDate noteEndDate = notedPeriodDTO.getEndDate() == null || notedPeriodDTO.getEndDate().isBlank() ?
                LocalDate.now() :
                LocalDate.parse(notedPeriodDTO.getEndDate());
        for (Habit habit : habits) {
            result.put(habit.getName(), statisticRepository.getHit(habit.getId(), noteBeginDate, noteEndDate));
        }
        return result;
    }
}

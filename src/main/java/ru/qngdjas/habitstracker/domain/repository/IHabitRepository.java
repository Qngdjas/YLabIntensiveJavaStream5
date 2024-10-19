package ru.qngdjas.habitstracker.domain.repository;

import ru.qngdjas.habitstracker.domain.model.Habit;
import ru.qngdjas.habitstracker.domain.repository.core.model.IModelRepository;

import java.util.List;

public interface IHabitRepository extends IModelRepository<Habit> {

    Habit retrieveByUserIDAndName(long userID, String name);

    List<Habit> listByUserID(long userID);

    boolean isExists(long userID, String habitName);
}

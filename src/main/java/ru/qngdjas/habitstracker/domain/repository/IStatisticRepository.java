package ru.qngdjas.habitstracker.domain.repository;

import ru.qngdjas.habitstracker.domain.model.Habit;

import java.time.LocalDate;

public interface IStatisticRepository {

    LocalDate note(long habitID, LocalDate date);

    long getStreak(long habitID);

    double getHit(long habitID, LocalDate beginDate, LocalDate endDate);

    boolean isNoted(long habitID, LocalDate date);
}

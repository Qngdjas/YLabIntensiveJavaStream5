package ru.qngdjas.habitstracker.domain.repository;

import java.time.LocalDate;

public interface IHabitNotesRepository {

    LocalDate note(long habitID, LocalDate date);

    long getStreak(long habitID);

    double getHit(long habitID, LocalDate beginDate, LocalDate endDate);

}

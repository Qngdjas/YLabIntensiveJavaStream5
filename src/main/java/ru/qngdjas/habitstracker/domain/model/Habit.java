package ru.qngdjas.habitstracker.domain.model;

import ru.qngdjas.habitstracker.domain.model.core.Model;

import java.time.LocalDate;

public class Habit extends Model {

    private String name;
    private String description;
    private boolean isDaily;
    private final LocalDate createdAt;
    private final long userId;

    public Habit(String name, String description, boolean isDaily, long userId) {
        this(-1, name, description, isDaily, LocalDate.now(), userId);
    }

    public Habit(long id, String name, String description, boolean isDaily, LocalDate createdAt, long userId) {
        super(id);
        this.name = name;
        this.description = description;
        this.isDaily = isDaily;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public String getPeriodicity() {
        return isDaily ? "ежедневная" : "еженедельная";
    }

    public int getRange() {
        return isDaily ? 1 : 7;
    }

    public void setDaily(boolean daily) {
        isDaily = daily;
    }

    public long getUserID() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("Привычка %s", name);
    }

}

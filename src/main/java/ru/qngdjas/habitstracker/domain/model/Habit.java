package ru.qngdjas.habitstracker.domain.model;

import ru.qngdjas.habitstracker.domain.model.core.Model;

import java.time.LocalDate;

public class Habit extends Model {

    private String name;
    private String description;
    private boolean isDaily;
    private LocalDate createdAt;
    private long userId;

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

    public void setDaily(boolean daily) {
        isDaily = daily;
    }

    public String getPeriodicity() {
        return isDaily ? "ежедневная" : "еженедельная";
    }

    public int getRange() {
        return isDaily ? 1 : 7;
    }


    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format("Привычка %s", name);
    }
}

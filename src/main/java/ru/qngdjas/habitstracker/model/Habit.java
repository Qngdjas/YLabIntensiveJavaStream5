package ru.qngdjas.habitstracker.model;

import java.time.LocalDate;

public class Habit {

    private String name;
    private String description;
    private boolean isDaily;
    private final LocalDate createdAt = LocalDate.now();

    public Habit(String name, String description, boolean isDaily) {
        this.name = name;
        this.description = description;
        this.isDaily = isDaily;
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

    public String getPeriodicity() {
        return isDaily ? "ежедневная" : "еженедельная";
    }

    public int getRange() {
        return isDaily ? 1 : 7;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public void setDaily(boolean daily) {
        isDaily = daily;
    }

    @Override
    public String toString() {
        return String.format("Привычка %s", name);
    }
}

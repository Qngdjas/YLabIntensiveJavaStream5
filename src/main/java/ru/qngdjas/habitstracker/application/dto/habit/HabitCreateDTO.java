package ru.qngdjas.habitstracker.application.dto.habit;

public class HabitCreateDTO {

    private String name;
    private String description;
    private boolean isDaily = true;
    private long userId;

    public HabitCreateDTO(String name, String description, boolean isDaily, long userId) {
        setName(name);
        setDescription(description);
        setDaily(isDaily);
        setUserId(userId);
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

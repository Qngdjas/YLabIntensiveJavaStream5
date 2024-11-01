package ru.qngdjas.habitstracker.application.dto.habit;

public class NotedDateDTO {

    private String date;

    public NotedDateDTO(String date) {
        setDate(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package ru.qngdjas.habitstracker.application.dto.message;

public class SingleMessageDTO {

    String message;

    public SingleMessageDTO(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

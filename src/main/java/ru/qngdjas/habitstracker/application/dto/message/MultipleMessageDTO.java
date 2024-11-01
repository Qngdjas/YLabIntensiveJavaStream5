package ru.qngdjas.habitstracker.application.dto.message;

import java.util.Map;

public class MultipleMessageDTO {

    Map<String, String> message;

    public MultipleMessageDTO(Map<String, String> message) {
        setMessage(message);
    }

    public Map<String, String> getMessage() {
        return message;
    }

    public void setMessage(Map<String, String> message) {
        this.message = Map.copyOf(message);
    }

}

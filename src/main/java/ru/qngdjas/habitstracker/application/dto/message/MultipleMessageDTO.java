package ru.qngdjas.habitstracker.application.dto.message;

import lombok.*;

import java.util.Map;


/**
 * Хранилище данных сообщений.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class MultipleMessageDTO {

    private Map<String, String> message;

//    public void setMessage(Map<String, String> message) {
//        this.message = Map.copyOf(message);
//    }

}

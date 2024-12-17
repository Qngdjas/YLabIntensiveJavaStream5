package ru.qngdjas.habitstracker.application.dto.message;

import lombok.*;

/**
 * Хранилище данных сообщений.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class SingleMessageDTO {

    private String message;
}

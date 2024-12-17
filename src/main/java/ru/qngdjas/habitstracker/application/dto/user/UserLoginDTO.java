package ru.qngdjas.habitstracker.application.dto.user;

import lombok.*;

/**
 * Хранилище данных авторизации.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class UserLoginDTO {

    private String email;
    private String password;

}

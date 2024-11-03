package ru.qngdjas.habitstracker.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Хранилище данных авторизации.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserLoginDTO {

    private String email;
    private String password;

}

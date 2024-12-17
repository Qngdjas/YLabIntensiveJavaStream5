package ru.qngdjas.habitstracker.application.dto.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class UserDTO {

    private long id;
    private String email;
    private String password;
    private String name;
    private boolean isAdmin;
}

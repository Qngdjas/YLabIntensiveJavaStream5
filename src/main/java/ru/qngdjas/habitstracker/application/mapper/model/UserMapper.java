package ru.qngdjas.habitstracker.application.mapper.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserDTO;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    User toUser(UserCreateDTO user) throws EmailException;

    @Mapping(target = "isAdmin", ignore = true)
    User toUser(UserDTO user) throws EmailException;
}

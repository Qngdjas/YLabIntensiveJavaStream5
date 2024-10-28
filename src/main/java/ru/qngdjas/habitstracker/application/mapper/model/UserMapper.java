package ru.qngdjas.habitstracker.application.mapper.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserUpdateDTO;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    User toUser(UserCreateDTO user) throws EmailException;

    @Mapping(target = "isAdmin", ignore = true)
    User toUser(UserUpdateDTO user) throws EmailException;
}

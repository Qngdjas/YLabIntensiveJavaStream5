package ru.qngdjas.habitstracker.application.mapper.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserUpdateDTO;
import ru.qngdjas.habitstracker.domain.model.user.EmailException;
import ru.qngdjas.habitstracker.domain.model.user.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserCreateDTO user) throws EmailException;

    User toUser(UserUpdateDTO user);
}

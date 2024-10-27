package ru.qngdjas.habitstracker.application.mapper.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.qngdjas.habitstracker.application.dto.user.LoginDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserUpdateDTO;

import java.io.IOException;
import java.io.InputStream;

public class UserJSONMapper {

    private final ObjectMapper objectMapper;

    public UserJSONMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public LoginDTO toLoginDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, LoginDTO.class);
    }

    public UserCreateDTO toUserCreateDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, UserCreateDTO.class);
    }

    public UserUpdateDTO toUserUpdateDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, UserUpdateDTO.class);
    }
}

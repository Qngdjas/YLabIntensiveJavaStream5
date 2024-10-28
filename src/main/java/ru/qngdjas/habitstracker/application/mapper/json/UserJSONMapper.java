package ru.qngdjas.habitstracker.application.mapper.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.qngdjas.habitstracker.application.dto.user.UserLoginDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserCreateDTO;
import ru.qngdjas.habitstracker.application.dto.user.UserUpdateDTO;

import java.io.IOException;
import java.io.InputStream;

public class UserJSONMapper {

    private final ObjectMapper objectMapper;

    public UserJSONMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public UserLoginDTO toLoginDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, UserLoginDTO.class);
    }

    public UserCreateDTO toUserCreateDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, UserCreateDTO.class);
    }

    public UserUpdateDTO toUserUpdateDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, UserUpdateDTO.class);
    }
}

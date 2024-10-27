package ru.qngdjas.habitstracker.application.mapper.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.qngdjas.habitstracker.application.dto.MessageDTO;

import java.io.IOException;

public class MessageJSONMapper {

    private final ObjectMapper objectMapper;

    public MessageJSONMapper() {
        objectMapper = new ObjectMapper();
    }

    public String toJson(MessageDTO messageDTO) throws IOException {
        return objectMapper.writeValueAsString(messageDTO);
    }

}

package ru.qngdjas.habitstracker.application.mapper.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.qngdjas.habitstracker.application.dto.message.MultipleMessageDTO;
import ru.qngdjas.habitstracker.application.dto.message.SingleMessageDTO;

import java.io.IOException;
import java.util.Map;

public class MessageJSONMapper {

    private final ObjectMapper objectMapper;

    public MessageJSONMapper() {
        objectMapper = new ObjectMapper();
    }

    public String toJson(SingleMessageDTO messageDTO) throws IOException {
        return objectMapper.writeValueAsString(messageDTO);
    }

    public String toJson(MultipleMessageDTO messageDTO) throws IOException {
        return objectMapper.writeValueAsString(messageDTO);
    }
}

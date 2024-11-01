package ru.qngdjas.habitstracker.application.mapper.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.qngdjas.habitstracker.application.dto.habit.HabitCreateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.application.dto.habit.NotedDateDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HabitJSONMapper {

    private final ObjectMapper objectMapper;

    public HabitJSONMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public HabitDTO toHabitDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, HabitDTO.class);
    }

    public HabitCreateDTO toHabitCreateDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, HabitCreateDTO.class);
    }

    public NotedDateDTO toNotedDateDTO(InputStream json) throws IOException {
        return objectMapper.readValue(json, NotedDateDTO.class);
    }

    public byte[] toJson(HabitDTO habitDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(habitDTO);
    }

    public byte[] toJson(List<HabitDTO> habitDTOS) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(habitDTOS);
    }

}

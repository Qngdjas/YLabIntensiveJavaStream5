package ru.qngdjas.habitstracker.application.mapper.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.qngdjas.habitstracker.application.dto.habit.HabitCreateDTO;
import ru.qngdjas.habitstracker.application.dto.habit.HabitDTO;
import ru.qngdjas.habitstracker.domain.model.Habit;

@Mapper
public interface HabitMapper {

    HabitMapper INSTANCE = Mappers.getMapper(HabitMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "isDaily", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Habit toHabit(HabitCreateDTO habitDTO);

    @Mapping(target = "description", ignore = true)
    @Mapping(target = "isDaily", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Habit toHabit(HabitDTO habitDTO);
}

package ru.qngdjas.habitstracker.domain.model.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Базовый класс моделей, обязующий использовать идентификаторы.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
abstract public class Model {

    private long id;

    /**
     * Сеттер идентификатора.
     * <p>Не допускает отрицательные идентификаторы.
     *
     * @param id - Уникальный идентификатор модели.
     */
    public void setId(long id) {
        if (id < 0) {
            throw new IdException();
        }
        this.id = id;
    }
}

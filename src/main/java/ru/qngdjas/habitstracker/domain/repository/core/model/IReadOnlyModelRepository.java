package ru.qngdjas.habitstracker.domain.repository.core.model;

import ru.qngdjas.habitstracker.domain.repository.core.base.IListRepository;
import ru.qngdjas.habitstracker.domain.repository.core.base.IRetrieveRepository;

/**
 * Интерфейс операций просмотра моделей репозитория.
 *
 * @param <Model> Предназначен для работы с моделями {@link Model}.
 */
public interface IReadOnlyModelRepository<Model> extends IRetrieveRepository<Model>, IListRepository<Model> {

}

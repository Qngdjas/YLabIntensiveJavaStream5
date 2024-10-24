package ru.qngdjas.habitstracker.domain.repository.core.model;

import ru.qngdjas.habitstracker.domain.repository.core.base.*;

/**
 * Интерфейс CRUD-операций над моделями репозитория.
 *
 * @param <Model> Предназначен для работы с моделями {@link Model}.
 */
public interface IModelRepository<Model> extends ICreateRepository<Model>, IUpdateRepository<Model>, IRetrieveRepository<Model>, IListRepository<Model>, IDeleteRepository {

}

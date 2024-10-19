package ru.qngdjas.habitstracker.domain.repository.core.model;

import ru.qngdjas.habitstracker.domain.repository.core.base.IListRepository;
import ru.qngdjas.habitstracker.domain.repository.core.base.IRetrieveRepository;

public interface IReadOnlyModelRepository<Model> extends IRetrieveRepository<Model>, IListRepository<Model> {

}

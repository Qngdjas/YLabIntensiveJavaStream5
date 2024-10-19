package ru.qngdjas.habitstracker.domain.repository.core.model;

import ru.qngdjas.habitstracker.domain.repository.core.base.*;

public interface IModelRepository<Model> extends ICreateRepository<Model>, IUpdateRepository<Model>, IRetrieveRepository<Model>, IListRepository<Model>, IDeleteRepository {

}

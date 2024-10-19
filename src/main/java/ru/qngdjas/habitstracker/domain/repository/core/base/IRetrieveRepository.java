package ru.qngdjas.habitstracker.domain.repository.core.base;

public interface IRetrieveRepository<T> {

    T retrieve(long id);
}

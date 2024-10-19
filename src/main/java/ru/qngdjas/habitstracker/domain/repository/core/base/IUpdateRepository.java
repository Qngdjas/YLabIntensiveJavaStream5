package ru.qngdjas.habitstracker.domain.repository.core.base;

public interface IUpdateRepository<T> {

    T update(T instance);

}

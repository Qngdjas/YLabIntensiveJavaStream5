package ru.qngdjas.habitstracker.domain.repository.core.base;

public interface ICreateRepository<T> {
    T create(T instance);
}

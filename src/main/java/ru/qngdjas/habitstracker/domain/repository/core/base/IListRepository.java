package ru.qngdjas.habitstracker.domain.repository.core.base;

import java.util.List;

public interface IListRepository<T> {

    List<T> list();
}

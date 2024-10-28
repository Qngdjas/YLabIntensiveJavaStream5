package ru.qngdjas.habitstracker.domain.model.core;

abstract public class Model {

    private long id;

    public Model(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

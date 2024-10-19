package ru.qngdjas.habitstracker.domain.model.core;

abstract public class Model {

    private long id;

    public Model(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }
}

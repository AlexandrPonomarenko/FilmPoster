package com.mongo.dao;

import java.util.List;

public abstract class AbstractDao<E, K> {
    public abstract List<E> getAll();
    public abstract E  update(E entity, K name);
    public abstract E getEntityById(K id);
    public abstract E getEntityByName(K name);
    public abstract boolean delete(K id);
    public abstract boolean create(E entity);
}

package fr.cyu.schoolmanagementsystem.service;

import java.util.List;

public interface GenericService<T, K> {
    K add(T entity);
    void delete(K id);
    void update(T entity);
    T getById(K id);
    List<T> getAll();
}

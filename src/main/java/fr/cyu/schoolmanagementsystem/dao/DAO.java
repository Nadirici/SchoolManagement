package fr.cyu.schoolmanagementsystem.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T,K> {
    K save(T e);
    void update(T e);
    void delete(T e);
    Optional<T> findById(K id);
    List<T> findAll();
}

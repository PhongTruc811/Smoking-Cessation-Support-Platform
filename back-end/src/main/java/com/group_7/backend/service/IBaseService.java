package com.group_7.backend.service;

import java.util.List;

public interface IBaseService<T,K> {
    T getById(K id);
    List<T> getAll();
    T update(K id, T t);
    void delete(K id);
}

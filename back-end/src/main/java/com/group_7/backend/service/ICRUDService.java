package com.group_7.backend.service;

public interface ICRUDService<T,K> extends IBaseService<T, K> {
    T create(T t);
}

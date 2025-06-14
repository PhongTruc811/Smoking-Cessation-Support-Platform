package com.group_7.backend.service;

import java.util.List;

public interface IBaseService<Res,Req, K> {
    Res getById(K id);
    List<Res> getAll();
    Res update(K id, Req t);
    void delete(K id);
}

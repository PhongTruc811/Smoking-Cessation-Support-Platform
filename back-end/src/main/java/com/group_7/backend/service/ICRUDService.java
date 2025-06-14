package com.group_7.backend.service;

public interface ICRUDService<Res, Req, K> extends IBaseService<Res, Req, K> {
    Res create(Req t);
}

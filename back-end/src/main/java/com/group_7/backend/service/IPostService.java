package com.group_7.backend.service;

import com.group_7.backend.dto.PostDto;

import java.util.List;

public interface IPostService extends ICRUDService<PostDto, PostDto, Long>{
    List<PostDto> getByUserId(Long userId);
}

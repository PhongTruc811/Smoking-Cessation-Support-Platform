package com.group_7.backend.service;

import com.group_7.backend.dto.PostDto;

import java.util.List;

public interface IPostService {
    List<PostDto> getByUserId(Long userId);
    PostDto create(PostDto postDto);
    PostDto getById(Long id);
    List<PostDto> getAll();
    PostDto update(Long id, PostDto postDto);
    void delete(Long id);
}

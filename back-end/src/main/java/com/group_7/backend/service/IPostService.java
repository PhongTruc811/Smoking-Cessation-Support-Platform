package com.group_7.backend.service;

import com.group_7.backend.dto.PostDto;

import java.util.List;

public interface IPostService {
    List<PostDto> getByUserId(Long userId);
}

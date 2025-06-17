package com.group_7.backend.service;

import com.group_7.backend.dto.CommentDto;

import java.util.List;

public interface ICommentService extends ICRUDService<CommentDto, CommentDto,Long>{
    List<CommentDto> getByPostId(Long postId);
}

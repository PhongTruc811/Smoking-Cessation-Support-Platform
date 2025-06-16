package com.group_7.backend.mapper;

import com.group_7.backend.dto.CommentDto;
import com.group_7.backend.entity.Comment;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    // Entity -> DTO
    public CommentDto toDto(Comment entity) {
        if (entity == null) return null;
        CommentDto dto = new CommentDto();
        dto.setCommentId(entity.getCommentId());
        dto.setParentCommentId(entity.getParentComment() != null ? entity.getParentComment().getCommentId() : null);
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        dto.setPostId(entity.getPost() != null ? entity.getPost().getPostId() : null);
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // DTO -> Entity (cần truyền User, Post, ParentComment nếu có)
    public Comment toEntity(CommentDto dto, User user, Post post, Comment parentComment) {
        if (dto == null) return null;
        Comment entity = new Comment();
        entity.setCommentId(dto.getCommentId());
        entity.setParentComment(parentComment);
        entity.setUser(user);
        entity.setPost(post);
        entity.setContent(dto.getContent());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}
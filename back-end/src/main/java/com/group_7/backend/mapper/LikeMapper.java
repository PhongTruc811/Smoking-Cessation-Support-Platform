package com.group_7.backend.mapper;

import com.group_7.backend.dto.LikeDto;
import com.group_7.backend.entity.Comment;
import com.group_7.backend.entity.Like;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {

    // Entity -> DTO
    public LikeDto toDto(Like entity) {
        if (entity == null) return null;
        LikeDto dto = new LikeDto();
        dto.setLikeId(entity.getLikeId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        dto.setPostId(entity.getPost() != null ? entity.getPost().getPostId() : null);
        dto.setCommentId(entity.getComment() != null ? entity.getComment().getCommentId() : null);
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // DTO -> Entity (cần truyền User, Post, Comment, vì chỉ có id trong dto)
    public Like toEntity(LikeDto dto, User user, Post post, Comment comment) {
        if (dto == null) return null;
        Like entity = new Like();
        entity.setLikeId(dto.getLikeId());
        entity.setUser(user);
        entity.setPost(post);
        entity.setComment(comment);
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}
package com.group_7.backend.mapper;

import com.group_7.backend.dto.PostDto;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    // Entity -> DTO
    public PostDto toDto(Post entity) {
        if (entity == null) return null;
        PostDto dto = new PostDto();
        dto.setPostId(entity.getPostId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setViewCount(entity.getViews());
        dto.setPublished(entity.isPublished());
        // Set author's full name for display
        if (entity.getUser() != null) {
            dto.setAuthorName(entity.getUser().getFullName());
        }
        return dto;
    }

    // DTO -> Entity (cần truyền User đã lấy từ DB)
    public Post toEntity(PostDto dto, User user) {
        if (dto == null) return null;
        Post entity = new Post();
        entity.setUser(user);
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setViews(dto.getViewCount());
        return entity;
    }
}
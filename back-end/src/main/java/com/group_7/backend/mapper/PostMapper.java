package com.group_7.backend.mapper;

import com.group_7.backend.dto.PostDto;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "userId", source = "user.userId")
    PostDto toResponseDto(Post entity);

    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "isPublished", source = "isPublished")
    Post toEntity(PostDto dto);

    @Named("userFromId")
    default User userFromId(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setUserId(id);
        return user;
    }
}
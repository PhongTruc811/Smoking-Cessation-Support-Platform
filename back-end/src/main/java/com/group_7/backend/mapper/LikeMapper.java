package com.group_7.backend.mapper;

import com.group_7.backend.dto.LikeDto;
import com.group_7.backend.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    // Entity -> ResponseDTO
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "postId", source = "post.postId")
    @Mapping(target = "commentId", source = "comment.commentId")
    LikeDto toDto(Like entity);

    // RequestDTO -> Entity
    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "post", source = "postId", qualifiedByName = "postFromId")
    @Mapping(target = "comment", source = "commentId", qualifiedByName = "commentFromId")
    @Mapping(target = "likeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Like toEntity(LikeDto dto);

    @Named("userFromId")
    default User userFromId(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setUserId(id);
        return user;
    }

    @Named("postFromId")
    default Post postFromId(Long id) {
        if (id == null) return null;
        Post post = new Post();
        post.setPostId(id);
        return post;
    }

    @Named("commentFromId")
    default Comment commentFromId(Long id) {
        if (id == null) return null;
        Comment comment = new Comment();
        comment.setCommentId(id);
        return comment;
    }
}
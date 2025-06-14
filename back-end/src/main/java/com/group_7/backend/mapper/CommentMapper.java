package com.group_7.backend.mapper;

import com.group_7.backend.dto.CommentDto;
import com.group_7.backend.entity.Comment;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    // Entity -> ResponseDTO
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "postId", source = "post.postId")
    @Mapping(target = "parentCommentId", source = "parentComment.commentId")
    CommentDto toDto(Comment entity);

    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "post", source = "postId", qualifiedByName = "postFromId")
    @Mapping(target = "parentComment", source = "parentCommentId", qualifiedByName = "commentFromId")
    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CommentDto dto);

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
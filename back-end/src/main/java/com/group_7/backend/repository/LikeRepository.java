package com.group_7.backend.repository;

import com.group_7.backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPost_PostId(Long postId);
    long countByComment_CommentId(Long commentId);

    Optional<Like> findByUser_UserIdAndPost_PostId(Long userId, Long postId);
    Optional<Like> findByUser_UserIdAndComment_CommentId(Long userId, Long commentId);

    List<Like> findByPost_PostId(Long postId);
    List<Like> findByComment_CommentId(Long commentId);
}
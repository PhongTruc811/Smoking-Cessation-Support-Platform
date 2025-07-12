package com.group_7.backend.repository;

import com.group_7.backend.entity.Like;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    int countByPostPostId(Long postId);

    boolean existsByPostAndUser(Post post, User user);

    boolean existsByPostPostIdAndUserUserId(Long postId, Long userId);

    Optional<Like> findByPostAndUser(Post post, User user);
}
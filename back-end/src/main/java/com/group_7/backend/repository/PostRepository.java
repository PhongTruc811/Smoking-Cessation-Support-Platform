package com.group_7.backend.repository;

import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserUserId(Long userId);
    // Count distinct users who have posted at least once (active members)
    @Query("SELECT COUNT(DISTINCT p.user.userId) FROM Post p")
    long countActiveMembers();

    // Count posts created since the start of this week (Monday)
    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= :startOfWeek")
    long countPostsThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek);

    List<Post> findByIsPublished(boolean isPublished);

}
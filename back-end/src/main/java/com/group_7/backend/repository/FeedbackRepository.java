package com.group_7.backend.repository;

import com.group_7.backend.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByUser_UserId(Long userUserId);
    List<Feedback> findByReceiverId(Long receiverId);

    boolean existsByReceiverIdAndUser_UserId(Long receiverId, Long userUserId);
    Feedback findByReceiverIdAndUser_UserId(Long receiverId, Long userUserId);

    @Query("SELECT COALESCE(AVG(f.rating), 0) FROM Feedback f WHERE f.receiverId = :receiverId")
    float findAverageRatingByReceiverId(Long receiverId);
    int countByReceiverId(Long receiverId);
}
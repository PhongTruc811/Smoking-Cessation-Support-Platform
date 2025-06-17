package com.group_7.backend.service;

public interface ILikeService {
    void like(Long postId, Long userId);
    void unlike(Long postId, Long userId);
    int countLikes(Long postId);
}

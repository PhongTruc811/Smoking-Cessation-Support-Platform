package com.group_7.backend.service.impl;

import com.group_7.backend.entity.Like;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.repository.LikeRepository;
import com.group_7.backend.repository.PostRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.ILikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeServiceImp implements ILikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeServiceImp(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void like(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (!likeRepository.existsByPostAndUser(post, user)) {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }
    }

    @Override
    @Transactional
    public void unlike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Optional<Like> like = likeRepository.findByPostAndUser(post, user);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
        }
    }

    @Override
    public int countLikes(Long postId) {
        return likeRepository.countByPostPostId(postId);
    }
    @Override
    public boolean hasUserLiked(Long postId, Long userId) {
    return likeRepository.existsByPostPostIdAndUserUserId(postId, userId);
}
}
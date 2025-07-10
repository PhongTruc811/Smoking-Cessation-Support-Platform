package com.group_7.backend.service.impl;

import com.group_7.backend.dto.PostDto;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.PostMapper;
import com.group_7.backend.repository.PostRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IPostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostServiceImp(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostDto getById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return postMapper.toDto(post);
    }

    @Override
    public List<PostDto> getAll() {
        return postRepository.findAll().stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PostDto create(PostDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        Post entity = postMapper.toEntity(dto, user);
        Post saved = postRepository.save(entity);
        return postMapper.toDto(saved);
    }

    @Override
    @Transactional
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PostDto update(Long postId, PostDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setPublished(dto.isPublished());
        Post saved = postRepository.save(post);
        return postMapper.toDto(saved);
    }

    @Override
    @Transactional
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        post.setPublished(false);
        postRepository.save(post);
    }

    @Override
    public List<PostDto> getByUserId(Long userId) {
        return postRepository.findByUserUserId(userId).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }
}
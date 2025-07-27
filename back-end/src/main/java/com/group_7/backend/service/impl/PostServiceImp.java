package com.group_7.backend.service.impl;

import com.group_7.backend.dto.PostDto;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.PostMapper;
import com.group_7.backend.repository.PostRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IPostService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public PostDto getById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return postMapper.toDto(post);
    }

    public List<PostDto> getAll() {
        return postRepository.findByIsPublished(true).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PostDto create(PostDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        Post entity = postMapper.toEntity(dto, user);
        Post saved = postRepository.save(entity);
        return postMapper.toDto(saved);
    }

    @Transactional
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PostDto update(Long postId, PostDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        //post.setPublished(dto.isPublished());
        Post saved = postRepository.save(post);
        return postMapper.toDto(saved);
    }
    
    @Transactional
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        post.setPublished(false);
        postRepository.save(post);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PostDto restore(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Đơn giản là đặt lại trạng thái published thành true
        post.setPublished(true);

        Post saved = postRepository.save(post);
        return postMapper.toDto(saved);
    }

    // --- PHƯƠNG THỨC MỚI DÀNH RIÊNG CHO ADMIN ---
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<PostDto> adminGetAll() {
        // Dùng findAll() để lấy TẤT CẢ, không lọc
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getByUserId(Long userId) {
        return postRepository.findByUserUserId(userId).stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // Start of the current week (Monday, 00:00)
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();

        long activeMembers = postRepository.countActiveMembers();
        long postsThisWeek = postRepository.countPostsThisWeek(startOfWeek);
        long totalPosts = postRepository.count();

        stats.put("activeMembers", activeMembers);
        stats.put("postsThisWeek", postsThisWeek);
        stats.put("totalPosts", totalPosts);

        return stats;
    }

}
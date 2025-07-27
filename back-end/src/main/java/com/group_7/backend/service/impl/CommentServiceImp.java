package com.group_7.backend.service.impl;

import com.group_7.backend.dto.CommentDto;
import com.group_7.backend.entity.Comment;
import com.group_7.backend.entity.Post;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.CommentMapper;
import com.group_7.backend.repository.CommentRepository;
import com.group_7.backend.repository.PostRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.ICommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImp implements ICommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImp(
            CommentRepository commentRepository,
            UserRepository userRepository,
            PostRepository postRepository,
            CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentDto getById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getAll() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getByPostId(Long postId) {
        return commentRepository.findByPostPostId(postId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto create(CommentDto dto) {
        // Get user and post
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + dto.getPostId()));
        
        // Get parent comment if this is a reply
        Comment parentComment = null;
        if (dto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + dto.getParentCommentId()));
        }
        
        // Create entity with parent comment
        Comment entity = commentMapper.toEntity(dto, user, post, parentComment);
        Comment saved = commentRepository.save(entity);
        return commentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CommentDto update(Long commentId, CommentDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        comment.setContent(dto.getContent());
        Comment saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        commentRepository.delete(comment);
    }
}
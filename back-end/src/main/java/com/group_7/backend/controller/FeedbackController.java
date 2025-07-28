package com.group_7.backend.controller;

import com.group_7.backend.dto.FeedbackDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IFeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final IFeedbackService feedbackService;

    public FeedbackController(IFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        FeedbackDto createdFeedback = feedbackService.create(feedbackDto);
        return new ResponseEntity<>(
                new ResponseDto("success", "Feedback created successfully", createdFeedback),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getFeedbackById(@PathVariable Long id) {
        FeedbackDto feedback = feedbackService.getById(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Feedback fetched successfully", feedback)
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllFeedbacks() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All feedbacks fetched successfully", feedbackService.getAll())
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDto> getFeedbacksByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Feedbacks by user fetched successfully", feedbackService.getByUserId(userId))
        );

    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<ResponseDto> getFeedbacksByReceiverId(@PathVariable Long receiverId) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Feedbacks by receiver fetched successfully", feedbackService.getByReceiverId(receiverId))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateFeedback(@PathVariable Long id, @Valid @RequestBody FeedbackDto feedbackDto) {
        FeedbackDto updatedFeedback = feedbackService.update(id, feedbackDto);
        return ResponseEntity.ok(
                new ResponseDto("success", "Feedback updated successfully", updatedFeedback)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteFeedback(@PathVariable Long id) {
        feedbackService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Feedback deleted successfully", null)
        );
    }
}
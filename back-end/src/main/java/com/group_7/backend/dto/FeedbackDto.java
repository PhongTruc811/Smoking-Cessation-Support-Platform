package com.group_7.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {

    private Long id;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    private String userName;

    @NotNull(message = "Receiver ID cannot be null")
    @Min(value = 0, message = "Receiver ID must be a valid ID")
    private Long receiverId;

    @NotBlank(message = "Subject cannot be empty")
    @Size(max = 255, message = "Subject cannot exceed 255 characters")
    private String subject;

    @NotBlank(message = "Feedback cannot be empty")
    private String body;

    @NotNull(message = "Rating is required!")
    private float rating;

    private LocalDateTime createdAt;
}
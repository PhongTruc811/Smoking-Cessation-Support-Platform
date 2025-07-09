package com.group_7.backend.entity.quiz;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Quizzes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @Column(name = "QuizId")
    private String quizId;

    @Column(name = "QuizName", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();
}




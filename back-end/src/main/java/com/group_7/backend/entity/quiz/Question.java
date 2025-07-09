package com.group_7.backend.entity.quiz;
import com.group_7.backend.entity.enums.QuestionTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class
Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestionId")
    private Long id;

    @Column(name = "QuestionText", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String questionText;

    @Column(name = "Answer", columnDefinition = "NVARCHAR(500)")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(name = "QuestionType", nullable = false)
    private QuestionTypeEnum questionTypeEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuizId")
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Option> options = new HashSet<>();


}
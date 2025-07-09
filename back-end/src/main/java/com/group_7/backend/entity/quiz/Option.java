package com.group_7.backend.entity.quiz;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "OptionText", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String optionText;

    @Column(name = "Score")
    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuestionId")
    private Question question;
}
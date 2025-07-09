package com.group_7.backend.entity;

import com.group_7.backend.entity.enums.QuestionTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "QuitMethods")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MethodID")
    private Long id;

    @Column(name = "MethodName", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String methodName;

    @Enumerated(EnumType.STRING)
    @Column(name = "MethodType", nullable = false)
    private QuestionTypeEnum methodType;

    //QuitMethod có nhiều Option
    @OneToMany(mappedBy = "quitMethod", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuitMethodOption> options = new HashSet<>();
}
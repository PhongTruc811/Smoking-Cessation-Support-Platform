package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QuitMethodOptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitMethodOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OptionID")
    private Long id;

    @Column(name = "OptionName", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String optionText;

    @Column(name = "OptionDescription", columnDefinition = "NVARCHAR(MAX)")
    private String optionDescription;

    @Column(name = "OptionNotification", columnDefinition = "NVARCHAR(MAX)")
    private String optionNoti;

    //1 Option thuộc 1 Method
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MethodID")
    private QuitMethod quitMethod;

    public QuitMethodOption(String optionText, String optionDescription, String optionNoti) {
        this.optionText = optionText;
        this.optionDescription = optionDescription;
        this.optionNoti = optionNoti;
    }
}
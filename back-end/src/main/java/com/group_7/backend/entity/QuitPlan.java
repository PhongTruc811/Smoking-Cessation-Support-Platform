package com.group_7.backend.entity;

import com.group_7.backend.entity.enums.QuitPlanStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Đại diện cho một kế hoạch cai thuốc của người dùng.
 */
@Entity
@Table(name = "QuitPlans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitPlan {

    @Id
    @Column(name = "PlanID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Quan hệ N-1 tới User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "Reason", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String reason;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "TargetEndDate")
    private LocalDate targetEndDate;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private QuitPlanStatusEnum status;

    //------------------------------------------------------------------------------
    @OneToMany(mappedBy = "quitPlan", cascade = CascadeType.ALL)
    private Set<QuitPlanStage> quitPlanStages = new HashSet<>();

//    @PrePersist
//    protected void onCreate() {
//        if (this.createdAt == null) {
//            this.createdAt = LocalDateTime.now();
//        }
//    }

    //Thêm PlanStage mới cho Plan
    public void addStage(QuitPlanStage stage) {
        quitPlanStages.add(stage);
        stage.setQuitPlan(this);
    }

    //Xóa PlanStage của Plan
    public void removeStage(QuitPlanStage stage) {
        quitPlanStages.remove(stage);
        stage.setQuitPlan(null);
    }

    //Những QuitPlan thuộc User này và ngược lại
    public void setUser(User user) {
        this.user = user;
        user.addQuitPlan(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuitPlan that = (QuitPlan) o;
        return id != 0 && id == that.id;
    }
}
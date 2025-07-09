package com.group_7.backend.entity;

import com.group_7.backend.entity.enums.QuitPlanStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet; // Thêm
import java.util.Set;    // Thêm
import java.util.List;

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
    private Long id;

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

    @Column(name = "DailySmoke")
    private int dailySmoke;

    //------------------------------------------------------------------------------

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "QuitPlan_QuitMethodOption",
            joinColumns = @JoinColumn(name = "PlanId"),
            inverseJoinColumns = @JoinColumn(name = "OptionId")
    )
    private Set<QuitMethodOption> methodOptions = new HashSet<>();

    // Một QuitPlan có nhiều QuitProgressLog
    @OneToMany(mappedBy = "quitPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuitProgressLog> progressLogs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuitPlan that = (QuitPlan) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
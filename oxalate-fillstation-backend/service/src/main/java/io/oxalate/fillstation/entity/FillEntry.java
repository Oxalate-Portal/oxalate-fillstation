package io.oxalate.fillstation.oxalate_fillstation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "fill_entries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FillEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "cylinder_id", nullable = false)
    private Long cylinderId;

    @Column(name = "fill_date", nullable = false)
    private LocalDate fillDate;

    @Column(name = "start_pressure", nullable = false, precision = 10, scale = 2)
    private BigDecimal startPressure;

    @Column(name = "end_pressure", nullable = false, precision = 10, scale = 2)
    private BigDecimal endPressure;

    @Column(name = "start_o2_percentage", nullable = false, precision = 6, scale = 2)
    private BigDecimal startO2Percentage;

    @Column(name = "start_he_percentage", nullable = false, precision = 6, scale = 2)
    private BigDecimal startHePercentage;

    @Column(name = "end_o2_percentage", nullable = false, precision = 6, scale = 2)
    private BigDecimal endO2Percentage;

    @Column(name = "end_he_percentage", nullable = false, precision = 6, scale = 2)
    private BigDecimal endHePercentage;

    @Column(name = "o2_added", precision = 12, scale = 4)
    private BigDecimal o2Added;

    @Column(name = "he_added", precision = 12, scale = 4)
    private BigDecimal heAdded;

    @Column(name = "gas_added", precision = 12, scale = 4)
    private BigDecimal gasAdded;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FillStatus status = FillStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

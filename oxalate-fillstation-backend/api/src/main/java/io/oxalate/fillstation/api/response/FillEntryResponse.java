package io.oxalate.fillstation.api.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FillEntryResponse {

    private Long id;
    private Long userId;
    private Long cylinderId;
    private LocalDate fillDate;
    private BigDecimal startPressure;
    private BigDecimal endPressure;
    private BigDecimal startO2Percentage;
    private BigDecimal startHePercentage;
    private BigDecimal endO2Percentage;
    private BigDecimal endHePercentage;
    private BigDecimal o2Added;
    private BigDecimal heAdded;
    private BigDecimal gasAdded;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean editable;
}

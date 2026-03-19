package io.oxalate.fillstation.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Fill entry details")
public class FillEntryResponse {

    @Schema(description = "Fill entry ID", example = "1")
    private Long id;
    @Schema(description = "User ID", example = "1")
    private Long userId;
    @Schema(description = "Cylinder ID", example = "1")
    private Long cylinderId;
    @Schema(description = "Date of fill", example = "2024-01-15")
    private LocalDate fillDate;
    @Schema(description = "Starting pressure in bar", example = "50.0")
    private BigDecimal startPressure;
    @Schema(description = "Ending pressure in bar", example = "232.0")
    private BigDecimal endPressure;
    @Schema(description = "Starting O2 percentage", example = "21.0")
    private BigDecimal startO2Percentage;
    @Schema(description = "Starting He percentage", example = "0.0")
    private BigDecimal startHePercentage;
    @Schema(description = "Ending O2 percentage", example = "32.0")
    private BigDecimal endO2Percentage;
    @Schema(description = "Ending He percentage", example = "0.0")
    private BigDecimal endHePercentage;
    @Schema(description = "O2 added in liters", example = "0.0")
    private BigDecimal o2Added;
    @Schema(description = "He added in liters", example = "0.0")
    private BigDecimal heAdded;
    @Schema(description = "Total gas added in liters", example = "0.0")
    private BigDecimal gasAdded;
    @Schema(description = "Optional notes")
    private String notes;
    @Schema(description = "Fill status", example = "ACTIVE")
    private String status;
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
    @Schema(description = "Whether the fill can still be edited")
    private boolean editable;
}

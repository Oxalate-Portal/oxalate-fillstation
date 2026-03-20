package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Fill entry data for create/update")
public class FillEntryRequest {

    @NotNull
    @Schema(description = "ID of the cylinder", example = "1")
    private Long cylinderId;

    @NotNull
    @Schema(description = "Date of the fill", example = "2024-01-15")
    private LocalDate fillDate;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Starting pressure in bar", example = "50.0")
    private BigDecimal startPressure;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Ending pressure in bar", example = "232.0")
    private BigDecimal endPressure;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Schema(description = "Starting O2 percentage", example = "21.0")
    private BigDecimal startO2Percentage;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Schema(description = "Starting He percentage", example = "0.0")
    private BigDecimal startHePercentage;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Schema(description = "Ending O2 percentage", example = "32.0")
    private BigDecimal endO2Percentage;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Schema(description = "Ending He percentage", example = "0.0")
    private BigDecimal endHePercentage;

    @Schema(description = "Optional notes", example = "Filled at station A")
    private String notes;
}

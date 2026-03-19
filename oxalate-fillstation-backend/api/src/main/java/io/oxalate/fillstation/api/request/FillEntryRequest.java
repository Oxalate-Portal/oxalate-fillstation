package io.oxalate.fillstation.api.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class FillEntryRequest {

    @NotNull
    private Long cylinderId;

    @NotNull
    private LocalDate fillDate;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal startPressure;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal endPressure;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal startO2Percentage;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal startHePercentage;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal endO2Percentage;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal endHePercentage;

    private String notes;
}

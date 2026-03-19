package io.oxalate.fillstation.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CylinderRequest {

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0.1")
    private BigDecimal volume;

    @NotNull
    @DecimalMin("0.1")
    private BigDecimal workingPressure;

    private String serialNumber;
}

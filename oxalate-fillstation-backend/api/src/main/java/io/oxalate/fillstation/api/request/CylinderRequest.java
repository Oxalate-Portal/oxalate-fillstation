package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Schema(description = "Cylinder data for create/update")
public class CylinderRequest {

    @NotBlank
    @Schema(description = "Cylinder name", example = "Main cylinder")
    private String name;

    @NotNull
    @DecimalMin("0.1")
    @Schema(description = "Cylinder volume in liters", example = "12.0")
    private BigDecimal volume;

    @NotNull
    @DecimalMin("0.1")
    @Schema(description = "Working pressure in bar", example = "232.0")
    private BigDecimal workingPressure;

    @Schema(description = "Cylinder serial number", example = "SN123456")
    private String serialNumber;
}

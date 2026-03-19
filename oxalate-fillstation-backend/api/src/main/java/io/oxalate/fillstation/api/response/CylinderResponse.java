package io.oxalate.fillstation.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cylinder details")
public class CylinderResponse {

    @Schema(description = "Cylinder ID", example = "1")
    private Long id;
    @Schema(description = "Owner user ID", example = "1")
    private Long userId;
    @Schema(description = "Cylinder name", example = "Main cylinder")
    private String name;
    @Schema(description = "Volume in liters", example = "12.0")
    private BigDecimal volume;
    @Schema(description = "Working pressure in bar", example = "232.0")
    private BigDecimal workingPressure;
    @Schema(description = "Serial number", example = "SN123456")
    private String serialNumber;
}

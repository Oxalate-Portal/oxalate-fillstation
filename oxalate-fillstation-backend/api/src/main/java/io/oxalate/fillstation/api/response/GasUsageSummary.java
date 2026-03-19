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
@Schema(description = "Gas usage statistics for a user")
public class GasUsageSummary {

    @Schema(description = "Total O2 added in liters", example = "0.0")
    private BigDecimal totalO2Added;
    @Schema(description = "Total He added in liters", example = "0.0")
    private BigDecimal totalHeAdded;
    @Schema(description = "Total gas added in liters", example = "0.0")
    private BigDecimal totalGasAdded;
    @Schema(description = "O2 added since last zero in liters", example = "0.0")
    private BigDecimal sinceLastZeroO2Added;
    @Schema(description = "He added since last zero in liters", example = "0.0")
    private BigDecimal sinceLastZeroHeAdded;
    @Schema(description = "Gas added since last zero in liters", example = "0.0")
    private BigDecimal sinceLastZeroGasAdded;
}

package io.oxalate.fillstation.api.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GasUsageSummary {

    private BigDecimal totalO2Added;
    private BigDecimal totalHeAdded;
    private BigDecimal totalGasAdded;
    private BigDecimal sinceLastZeroO2Added;
    private BigDecimal sinceLastZeroHeAdded;
    private BigDecimal sinceLastZeroGasAdded;
}

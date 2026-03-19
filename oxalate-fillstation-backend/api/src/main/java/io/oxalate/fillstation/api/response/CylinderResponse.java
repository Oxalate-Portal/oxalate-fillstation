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
public class CylinderResponse {

    private Long id;
    private Long userId;
    private String name;
    private BigDecimal volume;
    private BigDecimal workingPressure;
    private String serialNumber;
}

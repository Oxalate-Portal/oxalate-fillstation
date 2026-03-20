package io.oxalate.fillstation.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login history entry")
public class LoginHistoryResponse {

    @Schema(description = "Entry ID", example = "1")
    private Long id;
    @Schema(description = "Time of login")
    private LocalDateTime loginTime;
    @Schema(description = "IP address of login", example = "192.168.1.1")
    private String ipAddress;
}

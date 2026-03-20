package io.oxalate.fillstation.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Configuration entry details")
public class ConfigurationResponse {

    @Schema(description = "Configuration ID", example = "1")
    private Long id;
    @Schema(description = "Group name", example = "email")
    private String groupName;
    @Schema(description = "Key", example = "smtp.host")
    private String configKey;
    @Schema(description = "Value", example = "smtp.example.com")
    private String configValue;
}

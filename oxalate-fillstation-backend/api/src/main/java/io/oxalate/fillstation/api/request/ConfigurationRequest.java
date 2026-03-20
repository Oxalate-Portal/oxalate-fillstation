package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Configuration entry data")
public class ConfigurationRequest {

    @NotBlank
    @Schema(description = "Configuration group name", example = "email")
    private String groupName;

    @NotBlank
    @Schema(description = "Configuration key", example = "smtp.host")
    private String configKey;

    @NotBlank
    @Schema(description = "Configuration value", example = "smtp.example.com")
    private String configValue;
}

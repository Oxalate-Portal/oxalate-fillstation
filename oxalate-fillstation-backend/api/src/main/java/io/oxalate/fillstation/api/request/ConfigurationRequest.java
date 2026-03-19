package io.oxalate.fillstation.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfigurationRequest {

    @NotBlank
    private String groupName;

    @NotBlank
    private String configKey;

    @NotBlank
    private String configValue;
}

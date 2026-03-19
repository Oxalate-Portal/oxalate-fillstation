package io.oxalate.fillstation.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponse {

    private Long id;
    private String groupName;
    private String configKey;
    private String configValue;
}

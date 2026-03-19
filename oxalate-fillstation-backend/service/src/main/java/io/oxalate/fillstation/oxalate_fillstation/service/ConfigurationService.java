package io.oxalate.fillstation.oxalate_fillstation.service;

import io.oxalate.fillstation.oxalate_fillstation.entity.Configuration;
import io.oxalate.fillstation.oxalate_fillstation.repository.ConfigurationRepository;
import io.oxalate.fillstation.api.request.ConfigurationRequest;
import io.oxalate.fillstation.api.response.ConfigurationResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public List<ConfigurationResponse> getAll() {
        return configurationRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ConfigurationResponse create(ConfigurationRequest request) {
        Configuration config = Configuration.builder()
                .groupName(request.getGroupName())
                .configKey(request.getConfigKey())
                .configValue(request.getConfigValue())
                .build();
        return toResponse(configurationRepository.save(config));
    }

    public ConfigurationResponse update(Long id, ConfigurationRequest request) {
        Configuration config = configurationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuration not found"));
        config.setGroupName(request.getGroupName());
        config.setConfigKey(request.getConfigKey());
        config.setConfigValue(request.getConfigValue());
        return toResponse(configurationRepository.save(config));
    }

    public void delete(Long id) {
        if (!configurationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuration not found");
        }
        configurationRepository.deleteById(id);
    }

    private ConfigurationResponse toResponse(Configuration c) {
        return ConfigurationResponse.builder()
                .id(c.getId())
                .groupName(c.getGroupName())
                .configKey(c.getConfigKey())
                .configValue(c.getConfigValue())
                .build();
    }
}

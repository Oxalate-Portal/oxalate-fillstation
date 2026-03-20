package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.controller.AdminApi;
import io.oxalate.fillstation.api.request.ConfigurationRequest;
import io.oxalate.fillstation.api.response.ConfigurationResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.service.ConfigurationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final ConfigurationService configurationService;

    @Override
    public ResponseEntity<List<ConfigurationResponse>> getConfig() {
        return ResponseEntity.ok(configurationService.getAll());
    }

    @Override
    public ResponseEntity<ConfigurationResponse> createConfig(ConfigurationRequest request) {
        return ResponseEntity.ok(configurationService.create(request));
    }

    @Override
    public ResponseEntity<ConfigurationResponse> updateConfig(Long id, ConfigurationRequest request) {
        return ResponseEntity.ok(configurationService.update(id, request));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteConfig(Long id) {
        configurationService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Configuration deleted."));
    }
}

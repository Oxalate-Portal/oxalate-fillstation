package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.service.ConfigurationService;
import io.oxalate.fillstation.api.request.ConfigurationRequest;
import io.oxalate.fillstation.api.response.ConfigurationResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final ConfigurationService configurationService;

    @GetMapping("/config")
    public ResponseEntity<List<ConfigurationResponse>> getConfig() {
        return ResponseEntity.ok(configurationService.getAll());
    }

    @PostMapping("/config")
    public ResponseEntity<ConfigurationResponse> createConfig(@Valid @RequestBody ConfigurationRequest request) {
        return ResponseEntity.ok(configurationService.create(request));
    }

    @PutMapping("/config/{id}")
    public ResponseEntity<ConfigurationResponse> updateConfig(@PathVariable Long id,
                                                               @Valid @RequestBody ConfigurationRequest request) {
        return ResponseEntity.ok(configurationService.update(id, request));
    }

    @DeleteMapping("/config/{id}")
    public ResponseEntity<MessageResponse> deleteConfig(@PathVariable Long id) {
        configurationService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Configuration deleted."));
    }
}

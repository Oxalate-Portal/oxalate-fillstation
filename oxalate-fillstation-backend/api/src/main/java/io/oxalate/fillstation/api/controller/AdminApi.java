package io.oxalate.fillstation.api.controller;

import io.oxalate.fillstation.api.request.ConfigurationRequest;
import io.oxalate.fillstation.api.response.ConfigurationResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Admin", description = "Administrator configuration management")
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public interface AdminApi {

    @Operation(summary = "List all configuration entries")
    @ApiResponse(responseCode = "200", description = "Configuration list returned")
    @GetMapping("/config")
    ResponseEntity<List<ConfigurationResponse>> getConfig();

    @Operation(summary = "Create a new configuration entry")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuration created"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/config")
    ResponseEntity<ConfigurationResponse> createConfig(@Valid @RequestBody ConfigurationRequest request);

    @Operation(summary = "Update a configuration entry")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuration updated"),
        @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    @PutMapping("/config/{id}")
    ResponseEntity<ConfigurationResponse> updateConfig(@PathVariable Long id,
                                                        @Valid @RequestBody ConfigurationRequest request);

    @Operation(summary = "Delete a configuration entry")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuration deleted"),
        @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    @DeleteMapping("/config/{id}")
    ResponseEntity<MessageResponse> deleteConfig(@PathVariable Long id);
}

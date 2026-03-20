package io.oxalate.fillstation.api.controller;

import io.oxalate.fillstation.api.request.CylinderRequest;
import io.oxalate.fillstation.api.response.CylinderResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Cylinders", description = "Cylinder management for the current user")
@RequestMapping("/api/cylinders")
public interface CylinderApi {

    @Operation(summary = "List all cylinders for the current user")
    @ApiResponse(responseCode = "200", description = "List of cylinders returned")
    @GetMapping
    ResponseEntity<List<CylinderResponse>> getCylinders(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "Create a new cylinder")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cylinder created"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    ResponseEntity<CylinderResponse> createCylinder(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CylinderRequest request);

    @Operation(summary = "Update an existing cylinder")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cylinder updated"),
        @ApiResponse(responseCode = "404", description = "Cylinder not found")
    })
    @PutMapping("/{id}")
    ResponseEntity<CylinderResponse> updateCylinder(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody CylinderRequest request);

    @Operation(summary = "Delete a cylinder")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cylinder deleted"),
        @ApiResponse(responseCode = "404", description = "Cylinder not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<MessageResponse> deleteCylinder(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id);
}

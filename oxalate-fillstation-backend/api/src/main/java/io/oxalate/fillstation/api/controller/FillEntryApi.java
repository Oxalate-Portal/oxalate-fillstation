package io.oxalate.fillstation.api.controller;

import io.oxalate.fillstation.api.request.FillEntryRequest;
import io.oxalate.fillstation.api.response.FillEntryResponse;
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

@Tag(name = "Fill Entries", description = "Gas fill entry management")
@RequestMapping("/api/fills")
public interface FillEntryApi {

    @Operation(summary = "List all fill entries for the current user")
    @ApiResponse(responseCode = "200", description = "List of fill entries returned")
    @GetMapping
    ResponseEntity<List<FillEntryResponse>> getFills(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "Create a new fill entry")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fill entry created"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Cylinder not found")
    })
    @PostMapping
    ResponseEntity<FillEntryResponse> createFill(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody FillEntryRequest request);

    @Operation(summary = "Get a specific fill entry by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fill entry returned"),
        @ApiResponse(responseCode = "404", description = "Fill entry not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<FillEntryResponse> getFill(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id);

    @Operation(summary = "Update a fill entry", description = "Only allowed within 24 hours of creation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fill entry updated"),
        @ApiResponse(responseCode = "403", description = "Fill entry is no longer editable"),
        @ApiResponse(responseCode = "404", description = "Fill entry not found")
    })
    @PutMapping("/{id}")
    ResponseEntity<FillEntryResponse> updateFill(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody FillEntryRequest request);

    @Operation(summary = "Delete a fill entry", description = "Only allowed within 24 hours of creation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fill entry deleted"),
        @ApiResponse(responseCode = "403", description = "Fill entry is no longer editable"),
        @ApiResponse(responseCode = "404", description = "Fill entry not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<MessageResponse> deleteFill(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id);
}

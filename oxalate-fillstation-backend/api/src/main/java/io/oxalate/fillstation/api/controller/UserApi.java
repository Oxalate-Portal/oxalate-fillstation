package io.oxalate.fillstation.api.controller;

import io.oxalate.fillstation.api.request.UserUpdateRequest;
import io.oxalate.fillstation.api.response.GasUsageSummary;
import io.oxalate.fillstation.api.response.LoginHistoryResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Users", description = "User profile management")
@RequestMapping("/api/users")
public interface UserApi {

    @Operation(summary = "Get current user profile")
    @ApiResponse(responseCode = "200", description = "Current user profile returned")
    @GetMapping("/me")
    ResponseEntity<UserResponse> getMe(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "Update current user profile")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("/me")
    ResponseEntity<UserResponse> updateMe(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
                                          @Valid @RequestBody UserUpdateRequest request);

    @Operation(summary = "Get login history for the current user")
    @ApiResponse(responseCode = "200", description = "Login history returned")
    @GetMapping("/me/login-history")
    ResponseEntity<List<LoginHistoryResponse>> getLoginHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "Anonymize current user account", description = "Replaces PII with anonymous values per GDPR")
    @ApiResponse(responseCode = "200", description = "Account anonymized")
    @PostMapping("/me/anonymize")
    ResponseEntity<MessageResponse> anonymize(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "Get gas usage summary for a user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Gas usage summary returned"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}/gas-usage")
    @PreAuthorize("authentication.name == @userService.getUser(#id).email or hasAnyRole('ADMIN', 'OPERATOR')")
    ResponseEntity<GasUsageSummary> getGasUsage(@PathVariable Long id);
}

package io.oxalate.fillstation.api.controller;

import io.oxalate.fillstation.api.request.UserStatusRequest;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Operator", description = "Operator-level user and fill management")
@RequestMapping("/api/operator")
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
public interface OperatorApi {

    @Operation(summary = "List all users")
    @ApiResponse(responseCode = "200", description = "List of all users returned")
    @GetMapping("/users")
    ResponseEntity<List<UserResponse>> getAllUsers();

    @Operation(summary = "Get a specific user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User returned"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{id}")
    ResponseEntity<UserResponse> getUser(@PathVariable Long id);

    @Operation(summary = "Update a user's status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/users/{id}/status")
    ResponseEntity<UserResponse> updateUserStatus(@PathVariable Long id,
                                                   @Valid @RequestBody UserStatusRequest request);

    @Operation(summary = "List pending registrations")
    @ApiResponse(responseCode = "200", description = "List of pending registrations returned")
    @GetMapping("/registrations")
    ResponseEntity<List<UserResponse>> getPendingRegistrations();

    @Operation(summary = "Approve a pending registration")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registration approved"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/registrations/{id}/approve")
    ResponseEntity<UserResponse> approveRegistration(@PathVariable Long id);

    @Operation(summary = "Reject a pending registration")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registration rejected"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/registrations/{id}/reject")
    ResponseEntity<UserResponse> rejectRegistration(@PathVariable Long id);

    @Operation(summary = "Zero all fills for a user", description = "Marks all active and locked fills as zeroed and notifies the user by email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fills zeroed"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/users/{userId}/fills/zero")
    ResponseEntity<MessageResponse> zeroFills(@PathVariable Long userId);

    @Operation(summary = "Send gas usage notifications to all active users")
    @ApiResponse(responseCode = "200", description = "Notifications sent")
    @PostMapping("/notify-users")
    ResponseEntity<MessageResponse> notifyUsers();
}

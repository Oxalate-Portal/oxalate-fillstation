package io.oxalate.fillstation.api.controller;

import io.oxalate.fillstation.api.request.LoginRequest;
import io.oxalate.fillstation.api.request.PasswordChangeRequest;
import io.oxalate.fillstation.api.request.PasswordResetRequest;
import io.oxalate.fillstation.api.request.RegistrationRequest;
import io.oxalate.fillstation.api.response.LoginResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Authentication", description = "User registration, login, and password management")
@RequestMapping("/api/auth")
public interface AuthApi {

    @Operation(summary = "Register a new user", description = "Creates a new user account and sends a verification email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registration successful"),
        @ApiResponse(responseCode = "409", description = "Email already registered"),
        @ApiResponse(responseCode = "403", description = "Email is locked")
    })
    @PostMapping("/register")
    ResponseEntity<MessageResponse> register(@Valid @RequestBody RegistrationRequest request);

    @Operation(summary = "Login", description = "Authenticates a user and sets a JWT cookie")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "403", description = "Account pending or locked"),
        @ApiResponse(responseCode = "401", description = "Bad credentials")
    })
    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                        HttpServletRequest httpRequest,
                                        HttpServletResponse httpResponse);

    @Operation(summary = "Logout", description = "Clears the JWT authentication cookie")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    @PostMapping("/logout")
    ResponseEntity<MessageResponse> logout(HttpServletResponse response);

    @Operation(summary = "Verify email address", description = "Verifies the user email using the token from the verification email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Email verified"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @GetMapping("/verify-email")
    ResponseEntity<MessageResponse> verifyEmail(@RequestParam String token);

    @Operation(summary = "Request password reset", description = "Sends a password reset link to the user's email if it exists")
    @ApiResponse(responseCode = "200", description = "Reset link sent if email exists")
    @PostMapping("/forgot-password")
    ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody PasswordResetRequest request);

    @Operation(summary = "Reset password", description = "Sets a new password using a valid reset token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @PostMapping("/reset-password")
    ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody PasswordChangeRequest request);
}

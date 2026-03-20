package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.controller.AuthApi;
import io.oxalate.fillstation.api.request.LoginRequest;
import io.oxalate.fillstation.api.request.PasswordChangeRequest;
import io.oxalate.fillstation.api.request.PasswordResetRequest;
import io.oxalate.fillstation.api.request.RegistrationRequest;
import io.oxalate.fillstation.api.response.LoginResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<MessageResponse> register(RegistrationRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new MessageResponse("Registration successful. Please check your email."));
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request,
                                               HttpServletRequest httpRequest,
                                               HttpServletResponse httpResponse) {
        LoginResponse response = authService.login(request, httpRequest, httpResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MessageResponse> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok(new MessageResponse("Logged out successfully."));
    }

    @Override
    public ResponseEntity<MessageResponse> verifyEmail(String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(new MessageResponse("Email verified successfully."));
    }

    @Override
    public ResponseEntity<MessageResponse> forgotPassword(PasswordResetRequest request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok(new MessageResponse("If the email exists, a reset link has been sent."));
    }

    @Override
    public ResponseEntity<MessageResponse> resetPassword(PasswordChangeRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Password reset successfully."));
    }
}

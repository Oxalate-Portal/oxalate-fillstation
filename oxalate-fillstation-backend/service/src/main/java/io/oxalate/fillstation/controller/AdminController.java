package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.controller.AdminApi;
import io.oxalate.fillstation.api.request.ConfigurationRequest;
import io.oxalate.fillstation.api.response.ConfigurationResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.oxalate.fillstation.service.AuthService;
import io.oxalate.fillstation.service.ConfigurationService;
import io.oxalate.fillstation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final ConfigurationService configurationService;
    private final UserService userService;
    private final AuthService authService;

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserResponse> activateUser(Long id) {
        return ResponseEntity.ok(userService.activateUserAccount(id));
    }

    @Override
    public ResponseEntity<MessageResponse> sendPasswordReset(Long id) {
        authService.requestPasswordResetForUser(id);
        return ResponseEntity.ok(new MessageResponse("Password reset email sent."));
    }

    @Override
    public ResponseEntity<UserResponse> closeUser(Long id) {
        return ResponseEntity.ok(userService.closeUserAccount(id));
    }

    @Override
    public ResponseEntity<MessageResponse> anonymizeUser(Long id) {
        userService.anonymizeUserByAdmin(id);
        return ResponseEntity.ok(new MessageResponse("Account anonymized successfully."));
    }

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

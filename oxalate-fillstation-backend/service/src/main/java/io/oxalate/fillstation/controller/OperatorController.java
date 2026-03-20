package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.controller.OperatorApi;
import io.oxalate.fillstation.api.request.UserStatusRequest;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.oxalate.fillstation.service.EmailService;
import io.oxalate.fillstation.service.FillEntryService;
import io.oxalate.fillstation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OperatorController implements OperatorApi {

    private final UserService userService;
    private final FillEntryService fillEntryService;
    private final EmailService emailService;

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserResponse> getUser(Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Override
    public ResponseEntity<UserResponse> updateUserStatus(Long id, UserStatusRequest request) {
        return ResponseEntity.ok(userService.updateUserStatus(id, request.getStatus()));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getPendingRegistrations() {
        return ResponseEntity.ok(userService.getPendingRegistrations());
    }

    @Override
    public ResponseEntity<UserResponse> approveRegistration(Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, "ACTIVE"));
    }

    @Override
    public ResponseEntity<UserResponse> rejectRegistration(Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, "LOCKED"));
    }

    @Override
    public ResponseEntity<MessageResponse> zeroFills(Long userId) {
        UserResponse user = userService.getUser(userId);
        fillEntryService.zeroFills(userId, user.getEmail(), user.getName(), user.getLanguage());
        return ResponseEntity.ok(new MessageResponse("Fills zeroed for user " + userId));
    }

    @Override
    public ResponseEntity<MessageResponse> notifyUsers() {
        List<String> statuses = List.of("ACTIVE", "LOCKED");
        List<UserResponse> users = userService.getAllUsers().stream()
                .filter(u -> statuses.contains(u.getStatus()))
                .toList();
        for (UserResponse user : users) {
            var summary = fillEntryService.getGasUsage(user.getId());
            emailService.sendGasUsageNotificationEmail(
                    user.getEmail(), user.getName(), summary, user.getLanguage());
        }
        return ResponseEntity.ok(new MessageResponse("Notifications sent to " + users.size() + " users."));
    }
}

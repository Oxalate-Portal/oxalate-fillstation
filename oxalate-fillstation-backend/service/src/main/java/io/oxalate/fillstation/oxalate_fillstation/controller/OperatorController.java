package io.oxalate.fillstation.oxalate_fillstation.controller;

import io.oxalate.fillstation.oxalate_fillstation.entity.FillStatus;
import io.oxalate.fillstation.oxalate_fillstation.service.EmailService;
import io.oxalate.fillstation.oxalate_fillstation.service.FillEntryService;
import io.oxalate.fillstation.oxalate_fillstation.service.UserService;
import io.oxalate.fillstation.api.response.FillEntryResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operator")
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
@RequiredArgsConstructor
public class OperatorController {

    private final UserService userService;
    private final FillEntryService fillEntryService;
    private final EmailService emailService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/users/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(@PathVariable Long id,
                                                          @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.updateUserStatus(id, body.get("status")));
    }

    @GetMapping("/registrations")
    public ResponseEntity<List<UserResponse>> getPendingRegistrations() {
        return ResponseEntity.ok(userService.getPendingRegistrations());
    }

    @PostMapping("/registrations/{id}/approve")
    public ResponseEntity<UserResponse> approveRegistration(@PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, "ACTIVE"));
    }

    @PostMapping("/registrations/{id}/reject")
    public ResponseEntity<UserResponse> rejectRegistration(@PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, "LOCKED"));
    }

    @PostMapping("/users/{userId}/fills/zero")
    public ResponseEntity<MessageResponse> zeroFills(@PathVariable Long userId) {
        UserResponse user = userService.getUser(userId);
        fillEntryService.zeroFills(userId, user.getEmail(), user.getName(), user.getLanguage());
        return ResponseEntity.ok(new MessageResponse("Fills zeroed for user " + userId));
    }

    @PostMapping("/notify-users")
    public ResponseEntity<MessageResponse> notifyUsers(@RequestBody Map<String, Object> body) {
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

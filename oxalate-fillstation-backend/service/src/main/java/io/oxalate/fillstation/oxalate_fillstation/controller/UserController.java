package io.oxalate.fillstation.oxalate_fillstation.controller;

import io.oxalate.fillstation.oxalate_fillstation.service.FillEntryService;
import io.oxalate.fillstation.oxalate_fillstation.service.LoginHistoryService;
import io.oxalate.fillstation.oxalate_fillstation.service.UserService;
import io.oxalate.fillstation.api.request.UserUpdateRequest;
import io.oxalate.fillstation.api.response.GasUsageSummary;
import io.oxalate.fillstation.api.response.LoginHistoryResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginHistoryService loginHistoryService;
    private final FillEntryService fillEntryService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserByEmail(userDetails.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal UserDetails userDetails,
                                                  @Valid @RequestBody UserUpdateRequest request) {
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(userService.updateUser(user.getId(), request));
    }

    @GetMapping("/me/login-history")
    public ResponseEntity<List<LoginHistoryResponse>> getLoginHistory(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(loginHistoryService.getHistory(user.getId()));
    }

    @PostMapping("/me/anonymize")
    public ResponseEntity<MessageResponse> anonymize(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        userService.anonymizeUser(user.getId());
        return ResponseEntity.ok(new MessageResponse("Account anonymized successfully."));
    }

    @GetMapping("/{id}/gas-usage")
    public ResponseEntity<GasUsageSummary> getGasUsage(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        if (!me.getId().equals(id) && me.getRoles().stream().noneMatch(r -> r.contains("ADMIN") || r.contains("OPERATOR"))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(fillEntryService.getGasUsage(id));
    }
}

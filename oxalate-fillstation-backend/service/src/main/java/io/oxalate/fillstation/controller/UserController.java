package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.controller.UserApi;
import io.oxalate.fillstation.api.request.UserUpdateRequest;
import io.oxalate.fillstation.api.response.GasUsageSummary;
import io.oxalate.fillstation.api.response.LoginHistoryResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.oxalate.fillstation.service.FillEntryService;
import io.oxalate.fillstation.service.LoginHistoryService;
import io.oxalate.fillstation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final LoginHistoryService loginHistoryService;
    private final FillEntryService fillEntryService;

    @Override
    public ResponseEntity<UserResponse> getMe(UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserByEmail(userDetails.getUsername()));
    }

    @Override
    public ResponseEntity<UserResponse> updateMe(UserDetails userDetails, UserUpdateRequest request) {
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(userService.updateUser(user.getId(), request));
    }

    @Override
    public ResponseEntity<List<LoginHistoryResponse>> getLoginHistory(UserDetails userDetails) {
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(loginHistoryService.getHistory(user.getId()));
    }

    @Override
    public ResponseEntity<MessageResponse> anonymize(UserDetails userDetails) {
        UserResponse user = userService.getUserByEmail(userDetails.getUsername());
        userService.anonymizeUser(user.getId());
        return ResponseEntity.ok(new MessageResponse("Account anonymized successfully."));
    }

    @Override
    public ResponseEntity<GasUsageSummary> getGasUsage(Long id) {
        return ResponseEntity.ok(fillEntryService.getGasUsage(id));
    }
}

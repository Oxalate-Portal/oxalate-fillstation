package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.controller.FillEntryApi;
import io.oxalate.fillstation.api.request.FillEntryRequest;
import io.oxalate.fillstation.api.response.FillEntryResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.oxalate.fillstation.service.FillEntryService;
import io.oxalate.fillstation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FillEntryController implements FillEntryApi {

    private final FillEntryService fillEntryService;
    private final UserService userService;

    @Override
    public ResponseEntity<List<FillEntryResponse>> getFills(UserDetails userDetails) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(fillEntryService.getFills(me.getId()));
    }

    @Override
    public ResponseEntity<FillEntryResponse> createFill(UserDetails userDetails, FillEntryRequest request) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(fillEntryService.create(me.getId(), request));
    }

    @Override
    public ResponseEntity<FillEntryResponse> getFill(UserDetails userDetails, Long id) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(fillEntryService.getFill(me.getId(), id));
    }

    @Override
    public ResponseEntity<FillEntryResponse> updateFill(UserDetails userDetails, Long id, FillEntryRequest request) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(fillEntryService.update(me.getId(), id, request));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteFill(UserDetails userDetails, Long id) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        fillEntryService.delete(me.getId(), id);
        return ResponseEntity.ok(new MessageResponse("Fill entry deleted."));
    }
}

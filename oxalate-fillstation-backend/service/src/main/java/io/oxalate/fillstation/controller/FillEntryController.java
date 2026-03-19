package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.service.FillEntryService;
import io.oxalate.fillstation.service.UserService;
import io.oxalate.fillstation.api.request.FillEntryRequest;
import io.oxalate.fillstation.api.response.FillEntryResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fills")
@RequiredArgsConstructor
public class FillEntryController {

    private final FillEntryService fillEntryService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<FillEntryResponse>> getFills(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(fillEntryService.getFills(me.getId()));
    }

    @PostMapping
    public ResponseEntity<FillEntryResponse> createFill(@AuthenticationPrincipal UserDetails userDetails,
                                                         @Valid @RequestBody FillEntryRequest request) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(fillEntryService.create(me.getId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FillEntryResponse> getFill(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable Long id) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(fillEntryService.getFill(me.getId(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FillEntryResponse> updateFill(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable Long id,
                                                         @Valid @RequestBody FillEntryRequest request) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(fillEntryService.update(me.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFill(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable Long id) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        fillEntryService.delete(me.getId(), id);
        return ResponseEntity.ok(new MessageResponse("Fill entry deleted."));
    }
}

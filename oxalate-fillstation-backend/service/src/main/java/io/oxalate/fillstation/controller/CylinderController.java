package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.service.CylinderService;
import io.oxalate.fillstation.service.UserService;
import io.oxalate.fillstation.api.request.CylinderRequest;
import io.oxalate.fillstation.api.response.CylinderResponse;
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
@RequestMapping("/api/cylinders")
@RequiredArgsConstructor
public class CylinderController {

    private final CylinderService cylinderService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<CylinderResponse>> getCylinders(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(cylinderService.getCylinders(me.getId()));
    }

    @PostMapping
    public ResponseEntity<CylinderResponse> createCylinder(@AuthenticationPrincipal UserDetails userDetails,
                                                             @Valid @RequestBody CylinderRequest request) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(cylinderService.create(me.getId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CylinderResponse> updateCylinder(@AuthenticationPrincipal UserDetails userDetails,
                                                             @PathVariable Long id,
                                                             @Valid @RequestBody CylinderRequest request) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(cylinderService.update(me.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCylinder(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable Long id) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        cylinderService.delete(me.getId(), id);
        return ResponseEntity.ok(new MessageResponse("Cylinder deleted."));
    }
}

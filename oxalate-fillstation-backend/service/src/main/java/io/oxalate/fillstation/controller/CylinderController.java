package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.controller.CylinderApi;
import io.oxalate.fillstation.api.request.CylinderRequest;
import io.oxalate.fillstation.api.response.CylinderResponse;
import io.oxalate.fillstation.api.response.MessageResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.oxalate.fillstation.service.CylinderService;
import io.oxalate.fillstation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CylinderController implements CylinderApi {

    private final CylinderService cylinderService;
    private final UserService userService;

    @Override
    public ResponseEntity<List<CylinderResponse>> getCylinders(UserDetails userDetails) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(cylinderService.getCylinders(me.getId()));
    }

    @Override
    public ResponseEntity<CylinderResponse> createCylinder(UserDetails userDetails, CylinderRequest request) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(cylinderService.create(me.getId(), request));
    }

    @Override
    public ResponseEntity<CylinderResponse> updateCylinder(UserDetails userDetails, Long id, CylinderRequest request) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(cylinderService.update(me.getId(), id, request));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteCylinder(UserDetails userDetails, Long id) {
        UserResponse me = userService.getUserByEmail(userDetails.getUsername());
        cylinderService.delete(me.getId(), id);
        return ResponseEntity.ok(new MessageResponse("Cylinder deleted."));
    }
}

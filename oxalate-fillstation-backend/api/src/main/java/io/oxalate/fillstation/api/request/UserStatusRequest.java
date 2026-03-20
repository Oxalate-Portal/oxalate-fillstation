package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to update a user's status")
public class UserStatusRequest {
    @NotBlank
    @Schema(description = "New status value (ACTIVE or LOCKED)", example = "ACTIVE")
    private String status;
}

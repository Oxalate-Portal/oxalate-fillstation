package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Password reset request")
public class PasswordResetRequest {

    @NotBlank
    @Email
    @Schema(description = "Email address for reset link", example = "user@example.com")
    private String email;
}

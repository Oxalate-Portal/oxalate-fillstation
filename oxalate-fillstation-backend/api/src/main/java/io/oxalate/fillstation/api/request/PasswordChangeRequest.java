package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "New password with reset token")
public class PasswordChangeRequest {

    @NotBlank
    @Schema(description = "Password reset token from email", example = "abc123")
    private String token;

    @NotBlank
    @Size(min = 8)
    @Schema(description = "New password (min 8 characters)", example = "newpassword")
    private String newPassword;
}

package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Login credentials")
public class LoginRequest {

    @NotBlank
    @Email
    @Schema(description = "User email address", example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "User password", example = "mypassword")
    private String password;
}

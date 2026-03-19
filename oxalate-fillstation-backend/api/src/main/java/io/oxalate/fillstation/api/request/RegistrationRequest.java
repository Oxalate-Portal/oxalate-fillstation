package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "New user registration data")
public class RegistrationRequest {

    @NotBlank
    @Schema(description = "Full name", example = "John Doe")
    private String name;

    @NotBlank
    @Email
    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    @NotBlank
    @Size(min = 8)
    @Schema(description = "Password (min 8 characters)", example = "securepassword")
    private String password;

    @NotBlank
    @Schema(description = "Preferred language code", example = "en")
    private String language;
}

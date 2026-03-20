package io.oxalate.fillstation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User profile update data")
public class UserUpdateRequest {

    @NotBlank
    @Schema(description = "Full name", example = "John Doe")
    private String name;

    @NotBlank
    @Email
    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Preferred language code", example = "en")
    private String language;
}

package io.oxalate.fillstation.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Successful login response with user info")
public class LoginResponse {

    @Schema(description = "User ID", example = "1")
    private Long userId;
    @Schema(description = "User's full name", example = "John Doe")
    private String name;
    @Schema(description = "User's email", example = "john@example.com")
    private String email;
    @Schema(description = "List of user roles", example = "[\"ROLE_USER\"]")
    private List<String> roles;
    @Schema(description = "User's language preference", example = "en")
    private String language;
}

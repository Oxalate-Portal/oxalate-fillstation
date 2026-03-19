package io.oxalate.fillstation.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User details")
public class UserResponse {

    @Schema(description = "User ID", example = "1")
    private Long id;
    @Schema(description = "Full name", example = "John Doe")
    private String name;
    @Schema(description = "Email address", example = "john@example.com")
    private String email;
    @Schema(description = "Language preference", example = "en")
    private String language;
    @Schema(description = "Account status", example = "ACTIVE")
    private String status;
    @Schema(description = "List of roles", example = "[\"ROLE_USER\"]")
    private List<String> roles;
    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;
}

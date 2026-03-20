package io.oxalate.fillstation.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Simple message response")
public class MessageResponse {

    @Schema(description = "Response message", example = "Operation successful")
    private String message;
}

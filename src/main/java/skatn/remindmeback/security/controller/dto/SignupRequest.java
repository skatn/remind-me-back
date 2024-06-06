package skatn.remindmeback.security.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank @Size(min = 2, max = 14) String name,
        @NotBlank @Size(min = 4, max = 20) String username,
        @NotBlank @Size(min = 8, max = 255) String password,
        @NotBlank @Size(min = 8, max = 255) String passwordConfirm) {
}

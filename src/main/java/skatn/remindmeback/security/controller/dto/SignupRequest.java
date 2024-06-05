package skatn.remindmeback.security.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @Size(min = 1, max = 3) String name,
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String passwordConfirm) {
}

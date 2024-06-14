package skatn.remindmeback.common.fcm.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AddFcmTokenRequest(
        @NotBlank String token
) {
}

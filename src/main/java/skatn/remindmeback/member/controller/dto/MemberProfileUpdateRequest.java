package skatn.remindmeback.member.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberProfileUpdateRequest(
        @NotBlank @Size(min = 2, max = 14) String name
) {
}

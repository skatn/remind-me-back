package skatn.remindmeback.subject.contoller.dto;

import jakarta.validation.constraints.NotNull;

public record SubjectNotificationUpdateRequest(
        @NotNull Boolean enable
) {
}

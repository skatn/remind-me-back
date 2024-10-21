package skatn.remindmeback.subject.contoller.dto;

import jakarta.validation.constraints.Pattern;
import skatn.remindmeback.subject.entity.Visibility;

import java.util.List;

public record SubjectUpdateRequest(
        String title,
        @Pattern(regexp = "^[0-9A-Fa-f]{6}$") String color,
        Boolean isEnableNotification,
        Visibility visibility,
        List<String> tags
) {
}

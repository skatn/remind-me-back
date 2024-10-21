package skatn.remindmeback.subject.contoller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import skatn.remindmeback.subject.entity.Visibility;

import java.util.List;

public record SubjectCreateRequest(
        @NotBlank String title,
        @NotBlank @Pattern(regexp = "^[0-9A-Fa-f]{6}$") String color,
        @NotNull Visibility visibility,
        List<String> tags
) {
}

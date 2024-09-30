package skatn.remindmeback.subject.contoller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record SubjectUpdateRequest(
        @NotBlank String title,
        @NotBlank @Pattern(regexp = "^[0-9A-Fa-f]{6}$") String color,
        List<String> tags
) {
}

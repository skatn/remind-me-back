package skatn.remindmeback.question.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionMarkingRequest(
        @NotNull Long questionId,
        @NotBlank String submittedAnswer
) {
}

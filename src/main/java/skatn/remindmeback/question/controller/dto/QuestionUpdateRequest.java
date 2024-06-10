package skatn.remindmeback.question.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import skatn.remindmeback.question.dto.QuestionUpdateDto;
import skatn.remindmeback.question.entity.QuestionType;

import java.util.Set;
import java.util.stream.Collectors;

public record QuestionUpdateRequest(
        @NotBlank String question,
        @NotNull QuestionType questionType,
        @NotBlank String explanation,
        @NotNull Set<AnswerDto> answers
) {
    public record AnswerDto(String answer, boolean isAnswer) {
        public QuestionUpdateDto.AnswerDto toAnswerDto() {
            return new QuestionUpdateDto.AnswerDto(answer, isAnswer);
        }
    }

    public QuestionUpdateDto toQuestionUpdateDto(long questionId) {
        return new QuestionUpdateDto(
                questionId,
                question,
                questionType,
                explanation,
                answers.stream().map(AnswerDto::toAnswerDto).collect(Collectors.toSet())
        );
    }
}

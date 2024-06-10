package skatn.remindmeback.question.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import skatn.remindmeback.question.dto.QuestionCreateDto;
import skatn.remindmeback.question.entity.QuestionType;

import java.util.Set;
import java.util.stream.Collectors;

public record QuestionCreateRequest(
        @NotNull Long subjectId,
        @NotBlank String question,
        @NotNull QuestionType questionType,
        @NotBlank String explanation,
        @Valid @NotNull Set<AnswerDto> answers
) {
    public record AnswerDto(
            @NotBlank String answer,
            @NotNull Boolean isAnswer) {
        public QuestionCreateDto.AnswerDto toAnswerDto() {
            return new QuestionCreateDto.AnswerDto(answer, isAnswer);
        }
    }

    public QuestionCreateDto toQuestionCreateDto() {
        return new QuestionCreateDto(
                subjectId,
                question,
                questionType,
                explanation,
                answers.stream().map(AnswerDto::toAnswerDto).collect(Collectors.toSet())
        );
    }
}

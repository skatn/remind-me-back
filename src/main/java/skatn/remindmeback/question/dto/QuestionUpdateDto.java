package skatn.remindmeback.question.dto;

import skatn.remindmeback.question.entity.QuestionType;

import java.util.Set;

public record QuestionUpdateDto(
        long questionId,
        String question,
        QuestionType questionType,
        String explanation,
        Set<AnswerDto> answers
) {
    public record AnswerDto(String answer, boolean isAnswer) {
    }
}

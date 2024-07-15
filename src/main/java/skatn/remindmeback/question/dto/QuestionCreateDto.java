package skatn.remindmeback.question.dto;

import skatn.remindmeback.question.entity.QuestionType;

import java.util.Set;

public record QuestionCreateDto(
        long subjectId,
        String question,
        String questionImage,
        QuestionType questionType,
        String explanation,
        Set<AnswerDto> answers
) {
    public record AnswerDto(String answer, boolean isAnswer) {
    }
}

package skatn.remindmeback.question.dto;

import skatn.remindmeback.question.entity.Question;
import skatn.remindmeback.question.entity.QuestionType;

import java.util.Set;
import java.util.stream.Collectors;

public record QuestionDto(
        long id,
        QuestionType questionType,
        String explanation,
        Set<AnswerDto> answers
) {
    public QuestionDto(Question question) {
        this(
                question.getId(),
                question.getQuestionType(),
                question.getExplanation(),
                question.getAnswers().stream()
                        .map(AnswerDto::new)
                        .collect(Collectors.toSet())
        );
    }
}

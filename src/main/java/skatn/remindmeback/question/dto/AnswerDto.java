package skatn.remindmeback.question.dto;

import skatn.remindmeback.question.entity.Answer;

public record AnswerDto(
        long id,
        String answer,
        boolean isAnswer
) {
    public AnswerDto(Answer answer) {
        this(answer.getId(), answer.getAnswer(), answer.isAnswer());
    }
}

package skatn.remindmeback.question.repository.dto;

import skatn.remindmeback.question.entity.QuestionType;

public record QuestionScrollDto (
        long id,
        String question,
        QuestionType questionType
){
}

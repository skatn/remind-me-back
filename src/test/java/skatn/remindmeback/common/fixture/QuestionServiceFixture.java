package skatn.remindmeback.common.fixture;

import skatn.remindmeback.question.dto.QuestionCreateDto;
import skatn.remindmeback.question.dto.QuestionUpdateDto;
import skatn.remindmeback.question.entity.Answer;
import skatn.remindmeback.question.entity.Question;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.repository.dto.QuestionNotificationDto;

import java.util.List;
import java.util.Set;

public class QuestionServiceFixture {

    public static QuestionCreateDto createDto() {
        Set<QuestionCreateDto.AnswerDto> answers = Set.of(
                new QuestionCreateDto.AnswerDto("answer 1", true),
                new QuestionCreateDto.AnswerDto("answer 2", false),
                new QuestionCreateDto.AnswerDto("answer 3", false)
        );

        return new QuestionCreateDto(1L, "question", "image.png", QuestionType.CHOICE, "explanation", answers);
    }

    public static QuestionUpdateDto updateDto() {
        Set<QuestionUpdateDto.AnswerDto> answers = Set.of(
                new QuestionUpdateDto.AnswerDto("update answer 1", true),
                new QuestionUpdateDto.AnswerDto("update answer 2", false),
                new QuestionUpdateDto.AnswerDto("update answer 3", false)
        );

        return new QuestionUpdateDto(1L, "update question", "update_image.png", QuestionType.CHOICE, "update explanation", answers);
    }

    public static Question question() {
        Question question = Question.builder()
                .subject(SubjectFixture.subject())
                .id(1L)
                .question("question")
                .questionImage("/image.png")
                .questionType(QuestionType.CHOICE)
                .explanation("explanation")
                .build();

                question.changeAnswers(Set.of(
                        Answer.builder().id(1L).answer("answer 1").isAnswer(true).build(),
                        Answer.builder().id(2L).answer("answer 2").isAnswer(false).build(),
                        Answer.builder().id(3L).answer("answer 3").isAnswer(false).build()
                ));

        return question;
    }

    public static Question question(QuestionType questionType) {
        Set<Answer> choiceAnswers = Set.of(
                Answer.builder().id(1L).answer("answer 1").isAnswer(true).build(),
                Answer.builder().id(2L).answer("answer 2").isAnswer(false).build(),
                Answer.builder().id(3L).answer("answer 3").isAnswer(false).build()
        );
        Set<Answer> descriptiveAnswers = Set.of(Answer.builder().id(1L).answer("answer 1").isAnswer(true).build());

        Question question = Question.builder()
                .subject(SubjectFixture.subject())
                .id(1L)
                .question("question")
                .questionImage("/image.png")
                .questionType(questionType)
                .explanation("explanation")
                .build();

        question.changeAnswers(questionType == QuestionType.CHOICE ? choiceAnswers : descriptiveAnswers);

        return question;
    }

    public static List<QuestionNotificationDto> notificationDto() {
        return List.of(
                new QuestionNotificationDto(1L, 1L, "subject 1", "question 1", "fcm token"),
                new QuestionNotificationDto(2L, 1L, "subject 1", "question 2", "fcm token"),
                new QuestionNotificationDto(3L, 1L, "subject 1", "question 3", "fcm token"),
                new QuestionNotificationDto(4L, 1L, "subject 1", "question 4", "fcm token"),
                new QuestionNotificationDto(5L, 1L, "subject 1", "question 5", "fcm token")
        );
    }
}

package skatn.remindmeback.question.repository.dto;

public record QuestionNotificationDto(
        long questionId,
        long subjectId,
        String title,
        String body,
        String token
) {
}

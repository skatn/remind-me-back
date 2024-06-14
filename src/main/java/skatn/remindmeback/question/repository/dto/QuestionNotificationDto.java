package skatn.remindmeback.question.repository.dto;

public record QuestionNotificationDto(
        long id,
        String title,
        String body,
        String token
) {
}

package skatn.remindmeback.submithistory.repository.dto;

import java.time.LocalDate;

public record QuestionSubmitHistoryCountDto(
        LocalDate date,
        int count
) {
}

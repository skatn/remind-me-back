package skatn.remindmeback.subject.repository.dto;

import skatn.remindmeback.common.scroll.ScrollRequest;

import java.time.LocalDateTime;
import java.util.List;

public record SubjectListQueryCondition(
        ScrollRequest<LocalDateTime, Long> scroll,
        String title,
        List<String> tags
) {
}

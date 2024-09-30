package skatn.remindmeback.subject.repository.dto;

import skatn.remindmeback.common.scroll.ScrollRequest;

import java.util.List;

public record SubjectListQueryCondition(
        ScrollRequest<Long, Long> scroll,
        String title,
        List<String> tags
) {
}

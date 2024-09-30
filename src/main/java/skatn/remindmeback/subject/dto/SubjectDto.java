package skatn.remindmeback.subject.dto;

import skatn.remindmeback.subject.entity.Subject;

import java.util.List;

public record SubjectDto(Long id, String title, String color, boolean isEnableNotification, List<String> tags) {
    public SubjectDto(Subject subject) {
        this(
                subject.getId(),
                subject.getTitle(),
                subject.getColor(),
                subject.isEnableNotification(),
                subject.getTags().stream().map(tag -> tag.getTag().getName()).toList()
        );
    }
}

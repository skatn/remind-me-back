package skatn.remindmeback.subject.dto;

import skatn.remindmeback.subject.entity.Subject;

public record SubjectDto(Long id, String title, String cursor) {
    public SubjectDto(Subject subject) {
        this(subject.getId(), subject.getTitle(), subject.getColor());
    }

}

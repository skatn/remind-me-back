package skatn.remindmeback.subject.service.dto;

import skatn.remindmeback.subject.entity.Visibility;

import java.util.List;

public record SubjectUpdateDto(long subjectId, String title, String color, Visibility visibility, List<String> tags) {
}

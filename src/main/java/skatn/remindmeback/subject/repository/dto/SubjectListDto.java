package skatn.remindmeback.subject.repository.dto;

import java.util.List;

public record SubjectListDto(long id, String title, String color, long questionCount, List<String> tags) {
    public SubjectListDto(long id, String title, String color, long questionCount, String tags) {
        this(id, title, color, questionCount, tags == null ? List.of() : List.of(tags.split(",")));
    }
}

package skatn.remindmeback.subject.repository.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SubjectListDto(long id,
                             String title,
                             String color,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt,
                             long questionCount,
                             List<String> tags,
                             Author author
) {
    public SubjectListDto(long id,
                          String title,
                          String color,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt,
                          long questionCount,
                          String tags,
                          Author author) {

        this(
                id,
                title,
                color,
                createdAt,
                updatedAt,
                questionCount,
                tags == null ? List.of() : List.of(tags.split(",")),
                author);
    }

    public record Author(long id, String name) {
    }

}

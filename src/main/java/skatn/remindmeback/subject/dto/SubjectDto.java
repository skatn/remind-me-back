package skatn.remindmeback.subject.dto;

import skatn.remindmeback.subject.entity.Visibility;

import java.time.LocalDateTime;
import java.util.List;

public record SubjectDto(Long id,
                         String title,
                         String color,
                         boolean isEnableNotification,
                         Visibility visibility,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         long questionCount,
                         List<String> tags,
                         Author author) {

    public SubjectDto(Long id,
                      String title,
                      String color,
                      boolean isEnableNotification,
                      Visibility visibility,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      long questionCount,
                      String tags,
                      Author author) {

        this(
                id,
                title,
                color,
                isEnableNotification,
                visibility,
                createdAt,
                updatedAt,
                questionCount,
                tags == null ? List.of() : List.of(tags.split(",")),
                author
        );
    }

    public record Author(long id, String name) {
    }

}

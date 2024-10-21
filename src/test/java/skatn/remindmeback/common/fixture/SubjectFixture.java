package skatn.remindmeback.common.fixture;

import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.entity.Visibility;

import java.time.LocalDateTime;
import java.util.List;

public class SubjectFixture {

    public static Subject java() {
        return Subject.builder()
                .id(1L)
                .title("java")
                .color("000000")
                .author(MemberFixture.jake())
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();
    }

    public static SubjectDto subjectDto() {
        return new SubjectDto(
                1L,
                "java",
                "000000",
                true,
                Visibility.PUBLIC,
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0),
                10L,
                List.of("java", "programming"),
                new SubjectDto.Author(1L, "jake")
        );
    }

}

package skatn.remindmeback.common.fixture;

import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.entity.Subject;

import java.time.LocalDateTime;

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
        return new SubjectDto(java());
    }
}

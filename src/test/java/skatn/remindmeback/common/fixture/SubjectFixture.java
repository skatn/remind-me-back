package skatn.remindmeback.common.fixture;

import skatn.remindmeback.subject.entity.Subject;

import java.time.LocalDateTime;

public class SubjectFixture {

    public static Subject subject() {
        return Subject.builder()
                .id(1L)
                .title("subject1")
                .color("FF0000")
                .author(MemberFixture.member())
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();
    }
}

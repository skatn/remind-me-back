package skatn.remindmeback.common.fixture;

import skatn.remindmeback.member.entity.Member;

import java.time.LocalDateTime;

public class MemberFixture {

    public static Member jake() {
        return Member.builder()
                .id(1L)
                .name("jake")
                .username("jake")
                .password("jake_password")
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();
    }
}

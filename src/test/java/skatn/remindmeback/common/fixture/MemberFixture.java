package skatn.remindmeback.common.fixture;

import skatn.remindmeback.member.entity.Member;

import java.time.LocalDateTime;

public class MemberFixture {

    public static Member member() {
        return Member.builder()
                .id(1L)
                .name("member 1")
                .username("member1")
                .password("member1")
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();
    }
}

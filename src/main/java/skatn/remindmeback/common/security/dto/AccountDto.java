package skatn.remindmeback.common.security.dto;

import skatn.remindmeback.member.entity.Member;

public record AccountDto (
        Long id,
        String name,
        String username,
        String password,
        String role,
        boolean isActive,
        String provider
) {
    public AccountDto(Member member) {
        this(member.getId(), member.getName(), member.getUsername(), member.getPassword(), member.getRole(), member.isActive(), member.getProvider());
    }
}

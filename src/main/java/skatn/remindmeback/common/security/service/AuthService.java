package skatn.remindmeback.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.exception.AuthException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(String name, String username, String password) {
        if(memberRepository.findByUsername(username).isPresent()) {
            throw new AuthException(ErrorCode.ALREADY_USED_USERNAME);
        }

        Member member = Member.builder()
                .name(name)
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        return memberRepository.save(member).getId();
    }



}

package skatn.remindmeback.admin.initialize;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final AdminAccountProperties accountProperties;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if(memberRepository.findByUsername(accountProperties.username()).isEmpty()) {
            Member admin = Member.builder()
                    .username(accountProperties.username())
                    .password(passwordEncoder.encode(accountProperties.password()))
                    .name(accountProperties.name())
                    .role("ROLE_ADMIN")
                    .build();

            memberRepository.save(admin);
        }
    }
}

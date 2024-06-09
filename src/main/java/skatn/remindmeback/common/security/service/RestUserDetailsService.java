package skatn.remindmeback.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.security.dto.AccountContext;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.common.security.dto.AccountDto;

@Service
@RequiredArgsConstructor
public class RestUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found user"));

        return new AccountContext(new AccountDto(
                member.getId(),
                member.getName(),
                member.getUsername(),
                member.getPassword(),
                member.getRole(),
                member.isActive(),
                null)
        );
    }
}

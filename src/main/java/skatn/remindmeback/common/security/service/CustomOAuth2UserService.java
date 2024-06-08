package skatn.remindmeback.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import skatn.remindmeback.common.security.dto.AccountContext;
import skatn.remindmeback.common.security.dto.AccountDto;
import skatn.remindmeback.common.security.dto.OAuth2Attribute;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        Member member = findMemberOrJoin(oAuth2Attribute);

        return new AccountContext(new AccountDto(member), oAuth2User.getAttributes());
    }

    private Member findMemberOrJoin(OAuth2Attribute oAuth2Attribute) {
        return memberRepository.findByUsername(oAuth2Attribute.getUsername())
                .orElseGet(() -> memberRepository.save(Member.builder()
                                .name(oAuth2Attribute.getName())
                                .username(oAuth2Attribute.getUsername())
                                .provider(oAuth2Attribute.getProvider())
                                .build()
                        )
                );
    }
}

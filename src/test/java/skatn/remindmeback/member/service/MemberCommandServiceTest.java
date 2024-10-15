package skatn.remindmeback.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.fixture.MemberFixture;
import skatn.remindmeback.common.security.repository.RefreshTokenRepository;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class MemberCommandServiceTest {

    @InjectMocks
    MemberCommandService memberCommandService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("계정을 삭제한다.")
    void deleteAccount() {
        // given
        long memberId = 1L;
        Member member = MemberFixture.jake();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        doNothing().when(refreshTokenRepository).deleteByMemberId(anyLong());

        // when
        memberCommandService.deleteAccount(memberId);

        // then
        assertThat(member.isActive()).isFalse();
        then(refreshTokenRepository).should().deleteByMemberId(memberId);
    }

    @Test
    @DisplayName("계정을 삭제하려는 회원이 존재하지 않으면 예외가 발생한다")
    void deleteAccountFailWithoutMember() {
        // given
        long memberId = 1L;
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> memberCommandService.deleteAccount(memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("회원 이름을 변경한다.")
    void changeName() {
        // given
        long memberId = 1L;
        String newName = "newName";
        Member member = MemberFixture.jake();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        memberCommandService.update(memberId, newName);

        // then
        assertThat(member.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("이름을 변경하려는 회원이 존재하지 않으면 예외가 발생한다")
    void changeNameFailWithout() {
        // given
        long memberId = 1L;
        String newName = "newName";
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> memberCommandService.update(memberId, newName))
                .isInstanceOf(EntityNotFoundException.class);
    }

}
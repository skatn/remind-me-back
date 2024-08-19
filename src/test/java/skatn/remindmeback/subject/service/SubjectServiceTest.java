package skatn.remindmeback.subject.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.fixture.MemberFixture;
import skatn.remindmeback.common.fixture.SubjectFixture;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.repository.SubjectRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @InjectMocks
    SubjectService subjectService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    SubjectRepository subjectRepository;

    @Test
    @DisplayName("문제집을 생성한다")
    void create() {
        // given
        long authorId = 1L;
        String title = "title";
        String color = "FF0000";
        Subject subject = SubjectFixture.subject();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(MemberFixture.member()));
        given(subjectRepository.save(any())).willReturn(subject);

        // when
        long subjectId = subjectService.create(authorId, title, color);

        // then
        assertThat(subjectId).isEqualTo(subject.getId());
    }

    @Test
    @DisplayName("문제집 생성시 작성자가 존재하지 않으면 예외가 발생한다.")
    void createFailWithoutAuthor() {
        // given
        long authorId = 1L;
        String title = "title";
        String color = "FF0000";
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> subjectService.create(authorId, title, color))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제집을 단건 조회한다")
    void findOne() {
        // given
        Subject subject = SubjectFixture.subject();
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        SubjectDto subjectDto = subjectService.findOne(subject.getId());

        // then
        assertThat(subjectDto.id()).isEqualTo(subject.getId());
        assertThat(subjectDto.color()).isEqualTo(subject.getColor());
        assertThat(subjectDto.title()).isEqualTo(subject.getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 문제집을 단건 조회할 경우 예외가 발생한다")
    void findOneFailNotFound() {
        // given
        long subjectId = 1L;
        given(subjectRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> subjectService.findOne(subjectId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제집 정보를 수정한다")
    void update() {
        // given
        Subject subject = SubjectFixture.subject();
        String newTitle = "newTitle";
        String newColor = "00FF00";
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectService.update(subject.getId(), newTitle, newColor);

        // then
        assertThat(subject.getTitle()).isEqualTo(newTitle);
        assertThat(subject.getColor()).isEqualTo(newColor);
    }

    @Test
    @DisplayName("수정하려는 문제집이 존재하지 않으면 예외가 발생한다")
    void updateFailNotFound() {
        // given
        long subjectId = 1L;
        String newTitle = "newTitle";
        String newColor = "00FF00";
        given(subjectRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> subjectService.update(subjectId, newTitle, newColor))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제집을 삭제한다")
    void delete() {
        // given
        Subject subject = SubjectFixture.subject();
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectService.delete(subject.getId());

        // then
        then(subjectRepository).should().delete(subject);
    }

    @Test
    @DisplayName("삭제하려는 문제집이 존재하지 않으면 넘어간다")
    void deleteFailNotFound() {
        // given
        long subjectId = 1L;
        given(subjectRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        subjectService.delete(subjectId);

        // then
        then(subjectRepository).should(never()).delete(any());
    }

    @Test
    @DisplayName("문제집의 알림 설정 상태를 조회한다")
    void getNotificationStatus() {
        // given
        Subject subject = SubjectFixture.subject();
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        boolean status = subjectService.getNotificationStatus(subject.getId());

        // then
        assertThat(status).isEqualTo(subject.isEnableNotification());
    }

    @Test
    @DisplayName("알림 설정 상태를 조회하려는 문제집이 존재하지 않으면 예외가 발생한다")
    void getNotificationStatusFailNotFound() {
        // given
        long subjectId = 1L;
        given(subjectRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> subjectService.getNotificationStatus(subjectId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제집의 알림 설정 상태를 변경한다")
    void updateNotificationStatus() {
        // given
        boolean status = true;
        Subject subject = SubjectFixture.subject();
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectService.updateNotification(subject.getId(), status);

        // then
        assertThat(subject.isEnableNotification()).isEqualTo(status);
    }

    @Test
    @DisplayName("알림 설정 상태를 변경하려는 문제집이 존재하지 않으면 예외가 발생한다")
    void updateNotificationStatusFailNotFound() {
        // given
        boolean status = true;
        long subjectId = 1L;
        given(subjectRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> subjectService.updateNotification(subjectId, status))
                .isInstanceOf(EntityNotFoundException.class);
    }


}
package skatn.remindmeback.subject.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.fixture.SubjectFixture;
import skatn.remindmeback.common.fixture.TagFixture;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.entity.Visibility;
import skatn.remindmeback.subject.repository.SubjectRepository;
import skatn.remindmeback.subject.repository.TagRepository;
import skatn.remindmeback.subject.service.dto.SubjectCreateDto;
import skatn.remindmeback.subject.service.dto.SubjectUpdateDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectCommandServiceTest {

    @InjectMocks
    SubjectCommandService subjectCommandService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    SubjectRepository subjectRepository;
    @Mock
    TagRepository tagRepository;

    @Test
    @DisplayName("문제집을 생성한다")
    void create() {
        // given
        Subject subject = SubjectFixture.java();
        SubjectCreateDto subjectCreateDto = new SubjectCreateDto(subject.getAuthor().getId(), subject.getTitle(), subject.getColor(), Visibility.PUBLIC, List.of("java", "programming"));

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(subject.getAuthor()));
        given(subjectRepository.save(any())).willReturn(subject);
        given(tagRepository.findByName(anyString())).willReturn(Optional.empty());

        // when
        long subjectId = subjectCommandService.create(subjectCreateDto);

        // then
        assertThat(subjectId).isEqualTo(subject.getId());
    }

    @Test
    @DisplayName("문제집 생성시 작성자가 존재하지 않으면 예외가 발생한다.")
    void createFailWithoutAuthor() {
        // given
        Subject subject = SubjectFixture.java();
        SubjectCreateDto subjectCreateDto = new SubjectCreateDto(subject.getAuthor().getId(), subject.getTitle(), subject.getColor(), Visibility.PUBLIC, List.of("java", "programming"));

        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> subjectCommandService.create(subjectCreateDto))
                .isInstanceOf(EntityNotFoundException.class);
    }



    @Test
    @DisplayName("문제집 title을 수정한다")
    void updateTitle() {
        // given
        Subject subject = SubjectFixture.java();
        SubjectUpdateDto subjectUpdateDto = new SubjectUpdateDto(subject.getId(), "newTitle", null, null, null, null);
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectCommandService.update(subjectUpdateDto);

        // then
        assertThat(subject.getTitle()).isEqualTo("newTitle");
    }


    @Test
    @DisplayName("문제집 color를 수정한다")
    void updateColor() {
        // given
        Subject subject = SubjectFixture.java();
        SubjectUpdateDto subjectUpdateDto = new SubjectUpdateDto(subject.getId(), null, "00FF00", null, null, null);
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectCommandService.update(subjectUpdateDto);

        // then
        assertThat(subject.getColor()).isEqualTo("00FF00");
    }


    @Test
    @DisplayName("문제집 알림 여부를 수정한다")
    void updateEnableNotification() {
        // given
        Subject subject = SubjectFixture.java();
        SubjectUpdateDto subjectUpdateDto = new SubjectUpdateDto(subject.getId(), null, null, true, null, null);
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectCommandService.update(subjectUpdateDto);

        // then
        assertThat(subject.isEnableNotification()).isTrue();
    }


    @Test
    @DisplayName("문제집 공개 범위를 수정한다")
    void updateVisibility() {
        // given
        Subject subject = SubjectFixture.java();
        SubjectUpdateDto subjectUpdateDto = new SubjectUpdateDto(subject.getId(), null, null, null, Visibility.PUBLIC, null);
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectCommandService.update(subjectUpdateDto);

        // then
        assertThat(subject.getVisibility()).isEqualTo(Visibility.PUBLIC);
    }


    @Test
    @DisplayName("문제집 태그를 수정한다")
    void updateTag() {
        // given
        Subject subject = SubjectFixture.java();
        SubjectUpdateDto subjectUpdateDto = new SubjectUpdateDto(subject.getId(), null, null, null, null, List.of("java", "programming"));

        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));
        given(tagRepository.findByName(eq("java"))).willReturn(Optional.of(TagFixture.java()));
        given(tagRepository.findByName(eq("programming"))).willReturn(Optional.of(TagFixture.programming()));

        // when
        subjectCommandService.update(subjectUpdateDto);

        // then
        assertThat(subject.getTags())
                .extracting(subjectTag -> subjectTag.getTag().getName())
                .containsOnly("java", "programming");
    }

    @Test
    @DisplayName("수정하려는 문제집이 존재하지 않으면 예외가 발생한다")
    void updateFailNotFound() {
        // given
        Subject subject = SubjectFixture.java();
        SubjectUpdateDto subjectUpdateDto = new SubjectUpdateDto(subject.getId(), null, null, null, null, null);

        // when
        // then
        assertThatThrownBy(() -> subjectCommandService.update(subjectUpdateDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제집을 삭제한다")
    void delete() {
        // given
        Subject subject = SubjectFixture.java();
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectCommandService.delete(subject.getId());

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
        subjectCommandService.delete(subjectId);

        // then
        then(subjectRepository).should(never()).delete(any());
    }

    @Test
    @DisplayName("문제집의 알림 설정 상태를 조회한다")
    void getNotificationStatus() {
        // given
        Subject subject = SubjectFixture.java();
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        boolean status = subjectCommandService.getNotificationStatus(subject.getId());

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
        assertThatThrownBy(() -> subjectCommandService.getNotificationStatus(subjectId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제집의 알림 설정 상태를 변경한다")
    void updateNotificationStatus() {
        // given
        boolean status = true;
        Subject subject = SubjectFixture.java();
        given(subjectRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        subjectCommandService.updateNotification(subject.getId(), status);

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
        assertThatThrownBy(() -> subjectCommandService.updateNotification(subjectId, status))
                .isInstanceOf(EntityNotFoundException.class);
    }


}
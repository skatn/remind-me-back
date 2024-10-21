package skatn.remindmeback.subject.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.fixture.SubjectFixture;
import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.repository.SubjectQueryRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubjectQueryServiceTest {

    @InjectMocks
    SubjectQueryService subjectQueryService;

    @Mock
    SubjectQueryRepository subjectQueryRepository;


    @Test
    @DisplayName("문제집을 단건 조회한다")
    void findOne() {
        // given
        Subject subject = SubjectFixture.java();
        given(subjectQueryRepository.findById(anyLong())).willReturn(Optional.of(subject));

        // when
        SubjectDto subjectDto = subjectQueryService.getSubject(subject.getId());

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
        given(subjectQueryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> subjectQueryService.getSubject(subjectId))
                .isInstanceOf(EntityNotFoundException.class);
    }

}
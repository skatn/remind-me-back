package skatn.remindmeback.question.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.fixture.QuestionServiceFixture;
import skatn.remindmeback.question.dto.QuestionDto;
import skatn.remindmeback.question.entity.Question;
import skatn.remindmeback.question.repository.QuestionRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QuestionQueryServiceTest {

    @InjectMocks
    QuestionQueryService questionQueryService;

    @Mock
    QuestionRepository questionRepository;


    @Test
    @DisplayName("문제를 단건 조회한다")
    void findOne() {
        // given
        Question question = QuestionServiceFixture.question();
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));

        // when
        QuestionDto questionDto = questionQueryService.getQuestion(question.getId());

        // then
        assertThat(questionDto).isEqualTo(new QuestionDto(question));
    }

    @Test
    @DisplayName("존재하지 않는 문제를 조회하면 예외가 발생한다")
    void findOneFailNotFound() {
        // given
        long questionId = 1L;
        given(questionRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> questionQueryService.getQuestion(questionId))
                .isInstanceOf(EntityNotFoundException.class);
    }

}
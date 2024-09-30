package skatn.remindmeback.question.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.fcm.service.FcmService;
import skatn.remindmeback.common.fixture.QuestionServiceFixture;
import skatn.remindmeback.common.fixture.SubjectFixture;
import skatn.remindmeback.common.similarirty.SimilarityAnalyzer;
import skatn.remindmeback.question.dto.QuestionCreateDto;
import skatn.remindmeback.question.dto.QuestionDto;
import skatn.remindmeback.question.dto.QuestionUpdateDto;
import skatn.remindmeback.question.entity.Question;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.repository.QuestionQueryRepository;
import skatn.remindmeback.question.repository.QuestionRepository;
import skatn.remindmeback.question.repository.dto.QuestionNotificationDto;
import skatn.remindmeback.subject.repository.SubjectRepository;
import skatn.remindmeback.submithistory.repository.QuestionSubmitHistoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    QuestionCommandService questionCommandService;


    @Mock
    QuestionRepository questionRepository;
    @Mock
    SubjectRepository subjectRepository;
    @Mock
    SimilarityAnalyzer similarityAnalyzer;
    @Mock
    QuestionSubmitHistoryRepository questionSubmitHistoryRepository;
    @Mock
    QuestionQueryRepository questionQueryRepository;
    @Mock
    FcmService fcmService;

    @Test
    @DisplayName("문제를 생성한다")
    void create() {
        // given
        QuestionCreateDto questionCreateDto = QuestionServiceFixture.createDto();
        given(subjectRepository.findById(anyLong()))
                .willReturn(Optional.of(SubjectFixture.subject()));
        given(questionRepository.save(any())).willReturn(Question.builder().id(1L).build());

        // when
        long questionId = questionCommandService.create(questionCreateDto);

        // then
        assertThat(questionId).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 문제집에 문제를 생성하면 예외가 발생한다")
    void createFailNotFoundSubject() {
        // given
        QuestionCreateDto questionCreateDto = QuestionServiceFixture.createDto();
        given(subjectRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> questionCommandService.create(questionCreateDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제를 단건 조회한다")
    void findOne() {
        // given
        Question question = QuestionServiceFixture.question();
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));

        // when
        QuestionDto questionDto = questionCommandService.findOne(question.getId());

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
        assertThatThrownBy(() -> questionCommandService.findOne(questionId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제를 수정한다")
    void update() {
        // given
        Question question = QuestionServiceFixture.question();
        QuestionUpdateDto updateDto = QuestionServiceFixture.updateDto();
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));

        // when
        questionCommandService.update(updateDto);

        // then
        assertThat(question.getQuestion()).isEqualTo(updateDto.question());
        assertThat(question.getQuestionImage()).isEqualTo(updateDto.questionImage());
        assertThat(question.getQuestionType()).isEqualTo(updateDto.questionType());
        assertThat(question.getExplanation()).isEqualTo(updateDto.explanation());
        assertThat(question.getAnswers())
                .extracting("answer", "isAnswer")
                .contains(updateDto.answers().stream().map(dto -> tuple(dto.answer(), dto.isAnswer())).toArray(Tuple[]::new));
    }

    @Test
    @DisplayName("수정하려는 문제가 존재하지 않으면 예외가 발생한다")
    void updateFailNotFound() {
        // given
        QuestionUpdateDto updateDto = QuestionServiceFixture.updateDto();
        given(questionRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> questionCommandService.update(updateDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문제를 삭제한다")
    void delete() {
        // given
        Question question = QuestionServiceFixture.question();
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));

        // when
        questionCommandService.delete(question.getId());

        // then
        then(questionRepository).should().delete(question);
    }

    @Test
    @DisplayName("삭제하려는 문제가 존재하지 않는 경우 넘어간다")
    void deleteNotFound() {
        // given
        long questionId = 1L;
        given(questionRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        questionCommandService.delete(questionId);

        // then
        then(questionRepository).should(never()).delete(any());
    }

    @Test
    @DisplayName("제출한 답안이 정답이면 true를 반환한다")
    void submitCorrect() {
        // given
        String submittedAnswer = "answer 1";
        Question question = QuestionServiceFixture.question(QuestionType.CHOICE);
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));

        // when
        boolean isCorrect = questionCommandService.submit(question.getId(), submittedAnswer);

        // then
        assertThat(isCorrect).isTrue();
    }

    @Test
    @DisplayName("제출한 답안이 오답이면 false를 반환한다")
    void submitIncorrect() {
        // given
        String submittedAnswer = "incorrect";
        Question question = QuestionServiceFixture.question(QuestionType.DESCRIPTIVE);
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));

        // when
        boolean isCorrect = questionCommandService.submit(question.getId(), submittedAnswer);

        // then
        assertThat(isCorrect).isFalse();
    }

    @Test
    @DisplayName("문제 풀이 FCM Push 알림을 발송한다")
    void notification() {
        // given
        List<QuestionNotificationDto> notificationDtos = QuestionServiceFixture.notificationDto();
        given(questionQueryRepository.getQuestionsForNotification(any()))
                .willReturn(notificationDtos);

        // when
        questionCommandService.notification();

        // then
        then(fcmService).should(times(notificationDtos.size())).send(any(), any());
    }

}
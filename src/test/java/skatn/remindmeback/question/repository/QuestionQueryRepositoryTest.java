package skatn.remindmeback.question.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import skatn.remindmeback.common.fcm.entity.FcmToken;
import skatn.remindmeback.common.fcm.repository.FcmTokenRepository;
import skatn.remindmeback.common.fixture.MemberFixture;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.question.controller.dto.QuestionScrollRequest;
import skatn.remindmeback.question.entity.Question;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.repository.dto.QuestionNotificationDto;
import skatn.remindmeback.question.repository.dto.QuestionScrollDto;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.repository.SubjectRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionQueryRepositoryTest {

    @Autowired
    QuestionQueryRepository questionQueryRepository;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FcmTokenRepository fcmTokenRepository;

    @Test
    @DisplayName("문제 목록을 조회 한다")
    void scroll() {
        // given
        Member jake = memberRepository.save(MemberFixture.jake());
        Subject java = subjectRepository.save(Subject.builder().title("java 1").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 0)).build());
        List<Question> questions = questionRepository.saveAll(List.of(
                Question.builder().question("question 1").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 2").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 3").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 4").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 5").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 6").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 7").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 8").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 9").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 10").subject(java).questionType(QuestionType.DESCRIPTIVE).build()
        ));

        QuestionScrollRequest page1 = QuestionScrollRequest.builder().size(5).subjectId(java.getId()).build();
        QuestionScrollRequest page2 = QuestionScrollRequest.builder().size(5).subjectId(java.getId()).cursor(questions.get(5).getId()).build();

        // when
        Scroll<QuestionScrollDto> page1Result = questionQueryRepository.scrollQuestionList(page1.getSubjectId(), page1);
        Scroll<QuestionScrollDto> page2Result = questionQueryRepository.scrollQuestionList(page1.getSubjectId(), page2);

        // then
        assertThat(page1Result.content())
                .hasSize(5)
                .extracting(QuestionScrollDto::question)
                .containsExactlyInAnyOrder("question 1", "question 2", "question 3", "question 4", "question 5");
        assertThat(page1Result.nextCursor()).isEqualTo(questions.get(5).getId());
        assertThat(page1Result.nextSubCursor()).isNull();

        assertThat(page2Result.content())
                .hasSize(5)
                .extracting(QuestionScrollDto::question)
                .containsExactlyInAnyOrder("question 6", "question 7", "question 8", "question 9", "question 10");
        assertThat(page2Result.nextCursor()).isNull();
        assertThat(page2Result.nextSubCursor()).isNull();
    }

    @Test
    @DisplayName("Push 알림을 발송해야 할 문제 목록을 조회 한다")
    void getQuestionsForNotification() {
        // given
        Member jake = memberRepository.save(MemberFixture.jake());
        Subject java = subjectRepository.save(Subject.builder().title("java 1").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 0)).isEnableNotification(true).build());
        fcmTokenRepository.save(FcmToken.builder().member(jake).build());
        questionRepository.saveAll(List.of(
                Question.builder().question("question 1").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 2").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 3").subject(java).questionType(QuestionType.DESCRIPTIVE).build(),
                Question.builder().question("question 4").subject(java).questionType(QuestionType.DESCRIPTIVE).notificationTime(LocalDateTime.of(2024, 1, 1, 0, 0)).build(),
                Question.builder().question("question 5").subject(java).questionType(QuestionType.DESCRIPTIVE).notificationTime(LocalDateTime.of(2024, 1, 1, 0, 0)).build(),
                Question.builder().question("question 6").subject(java).questionType(QuestionType.DESCRIPTIVE).notificationTime(LocalDateTime.of(2024, 1, 1, 0, 1)).build()
        ));

        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 1);

        // when
        List<QuestionNotificationDto> questionsForNotification = questionQueryRepository.getQuestionsForNotification(time);

        // then
        assertThat(questionsForNotification)
                .hasSize(3)
                .extracting(QuestionNotificationDto::body)
                .containsExactlyInAnyOrder("question 4", "question 5", "question 6");

    }

    @TestConfiguration
    static class Config {

        @Bean
        QuestionQueryRepository questionQueryRepository(EntityManager entityManager) {
            return new QuestionQueryRepository(entityManager);
        }
    }

}
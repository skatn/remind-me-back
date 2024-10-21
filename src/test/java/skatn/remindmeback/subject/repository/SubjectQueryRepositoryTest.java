package skatn.remindmeback.subject.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import skatn.remindmeback.common.fixture.MemberFixture;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.question.entity.Question;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.repository.QuestionRepository;
import skatn.remindmeback.subject.contoller.dto.SubjectScrollRequest;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.entity.Tag;
import skatn.remindmeback.subject.repository.dto.SubjectListDto;
import skatn.remindmeback.subject.repository.dto.SubjectListQueryCondition;
import skatn.remindmeback.submithistory.entity.HistoryStatus;
import skatn.remindmeback.submithistory.entity.QuestionSubmitHistory;
import skatn.remindmeback.submithistory.repository.QuestionSubmitHistoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@SpringJUnitConfig
class SubjectQueryRepositoryTest {

    @Autowired
    SubjectQueryRepository subjectQueryRepository;

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionSubmitHistoryRepository questionSubmitHistoryRepository;

    @Nested
    class SingleSubject {

        @Test
        @DisplayName("문제집을 단건 조회한다")
        void findById() {
            // given
            Subject java = Subject.builder()
                    .title("java")
                    .color("000000")
                    .author(Member.builder()
                            .name("jake")
                            .username("jake")
                            .password("jake_password")
                            .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                            .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                            .build())
                    .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                    .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                    .build();

            memberRepository.save(java.getAuthor());
            subjectRepository.save(java);

            // when
            Optional<Subject> findJava = subjectQueryRepository.findById(java.getId());

            // then
            assertThat(findJava).isNotEmpty();
            assertThat(findJava.get().getId()).isEqualTo(java.getId());
            assertThat(findJava.get().getTitle()).isEqualTo(java.getTitle());
            assertThat(findJava.get().getColor()).isEqualTo(java.getColor());
        }

        @Test
        @DisplayName("조회하려는 문제집이 존재하지 않을 경우 Optional.empty를 반환한다")
        void findByIdNotFound() {
            // given

            // when
            Optional<Subject> findSubject = subjectQueryRepository.findById(1L);

            // then
            assertThat(findSubject).isEmpty();
        }

    }

    @Nested
    class MultipleSubjects {

        Member jake;
        List<Subject> subjects = new ArrayList<>();

        @BeforeEach
        void setup() {
            jake = memberRepository.save(MemberFixture.jake());
            Member jason = memberRepository.save(MemberFixture.jason());

            subjects = List.of(
                    Subject.builder().title("java 1").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 0)).build(),
                    Subject.builder().title("java 2").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 1)).build(),
                    Subject.builder().title("java 3").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 2)).build(),
                    Subject.builder().title("java 4").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 3)).build(),
                    Subject.builder().title("java 5").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 4)).build(),
                    Subject.builder().title("spring 1").color("000000").author(jason).createdAt(LocalDateTime.of(2024, 1, 1, 0, 5)).build(),
                    Subject.builder().title("spring 2").color("000000").author(jason).createdAt(LocalDateTime.of(2024, 1, 1, 0, 6)).build(),
                    Subject.builder().title("spring 3").color("000000").author(jason).createdAt(LocalDateTime.of(2024, 1, 1, 0, 7)).build(),
                    Subject.builder().title("spring 4").color("000000").author(jason).createdAt(LocalDateTime.of(2024, 1, 1, 0, 8)).build(),
                    Subject.builder().title("spring 5").color("000000").author(jason).createdAt(LocalDateTime.of(2024, 1, 1, 0, 9)).build()
            );

            Tag tagJava = Tag.builder().name("java").build();
            Tag tagSpring = Tag.builder().name("spring").build();
            Tag tagProgramming = Tag.builder().name("programming").build();

            subjects.stream()
                    .filter(subject -> subject.getTitle().contains("java"))
                    .forEach(subject -> subject.changeTags(List.of(tagJava, tagProgramming)));

            subjects.stream()
                    .filter(subject -> subject.getTitle().contains("spring"))
                    .forEach(subject -> subject.changeTags(List.of(tagSpring, tagProgramming)));

            tagRepository.saveAll(List.of(tagJava, tagSpring, tagProgramming));
            subjectRepository.saveAll(subjects);
        }

        @Test
        @DisplayName("문제집 목록을 조회 한다")
        void getSubjectList() {
            // given
            SubjectListQueryCondition page1Condition = new SubjectListQueryCondition(SubjectScrollRequest.builder().size(5).build(), null, null);
            SubjectListQueryCondition page2Condition = new SubjectListQueryCondition(SubjectScrollRequest.builder().size(5).cursor(subjects.get(4).getCreatedAt()).subCursor(subjects.get(4).getId()).build(), null, null);

            // when
            Scroll<SubjectListDto> page1 = subjectQueryRepository.scrollSubjectList(null, page1Condition);
            Scroll<SubjectListDto> page2 = subjectQueryRepository.scrollSubjectList(null, page2Condition);

            // then
            assertThat(page1.content())
                    .hasSize(5)
                    .extracting(SubjectListDto::title)
                    .containsExactlyInAnyOrder("spring 1", "spring 2", "spring 3", "spring 4", "spring 5");
            assertThat(page1.nextCursor()).isEqualTo(subjects.get(4).getCreatedAt());
            assertThat(page1.nextSubCursor()).isEqualTo(subjects.get(4).getId());

            assertThat(page2.content())
                    .hasSize(5)
                    .extracting(SubjectListDto::title)
                    .containsExactlyInAnyOrder("java 1", "java 2", "java 3", "java 4", "java 5");
            assertThat(page2.nextCursor()).isNull();
            assertThat(page2.nextSubCursor()).isNull();
        }


        @Test
        @DisplayName("문제집 목록은 최신순으로 정렬 된다")
        void orderedRecently() {
            // given
            SubjectListQueryCondition condition = new SubjectListQueryCondition(SubjectScrollRequest.builder().size(10).build(), null, null);


            // when
            Scroll<SubjectListDto> subjectList = subjectQueryRepository.scrollSubjectList(null, condition);

            // then
            assertThat(subjectList.content())
                    .hasSize(10)
                    .extracting(SubjectListDto::createdAt)
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }

        @Test
        @DisplayName("제목을 검색할 수 있다")
        void filteredByTitle() {
            // given
            SubjectListQueryCondition condition = new SubjectListQueryCondition(SubjectScrollRequest.builder().size(10).build(), "java", null);

            // when
            Scroll<SubjectListDto> subjectList = subjectQueryRepository.scrollSubjectList(null, condition);

            // then
            assertThat(subjectList.content())
                    .hasSize(5)
                    .extracting(SubjectListDto::title)
                    .containsExactlyInAnyOrder("java 1", "java 2", "java 3", "java 4", "java 5");
        }

        @Test
        @DisplayName("특정 태그가 지정된 문제집 목록만 조회할 수 있다")
        void filteredByTag() {
            // given
            SubjectListQueryCondition java = new SubjectListQueryCondition(SubjectScrollRequest.builder().size(10).build(), null, List.of("java"));
            SubjectListQueryCondition spring = new SubjectListQueryCondition(SubjectScrollRequest.builder().size(10).build(), null, List.of("spring"));
            SubjectListQueryCondition programming = new SubjectListQueryCondition(SubjectScrollRequest.builder().size(10).build(), null, List.of("programming"));

            // when
            Scroll<SubjectListDto> filteredJavaTag = subjectQueryRepository.scrollSubjectList(null, java);
            Scroll<SubjectListDto> filteredSpringTag = subjectQueryRepository.scrollSubjectList(null, spring);
            Scroll<SubjectListDto> filteredProgrammingTag = subjectQueryRepository.scrollSubjectList(null, programming);

            // then
            assertThat(filteredJavaTag.content())
                    .hasSize(5)
                    .extracting(SubjectListDto::title)
                    .containsExactlyInAnyOrder("java 1", "java 2", "java 3", "java 4", "java 5");

            assertThat(filteredSpringTag.content())
                    .hasSize(5)
                    .extracting(SubjectListDto::title)
                    .containsExactlyInAnyOrder("spring 1", "spring 2", "spring 3", "spring 4", "spring 5");

            assertThat(filteredProgrammingTag.content())
                    .hasSize(10)
                    .extracting(SubjectListDto::title)
                    .containsExactlyInAnyOrder("java 1", "java 2", "java 3", "java 4", "java 5", "spring 1", "spring 2", "spring 3", "spring 4", "spring 5");
        }

        @Test
        @DisplayName("특정 사용자가 생성한 문제집 목록만 조회할 수 있다")
        void filteredByAuthor() {
            // given
            SubjectListQueryCondition condition = new SubjectListQueryCondition(SubjectScrollRequest.builder().size(10).build(), null, null);

            // when
            Scroll<SubjectListDto> subjectList = subjectQueryRepository.scrollSubjectList(jake.getId(), condition);

            // then
            assertThat(subjectList.content())
                    .hasSize(5)
                    .extracting(SubjectListDto::title)
                    .containsExactlyInAnyOrder("java 1", "java 2", "java 3", "java 4", "java 5");
        }
    }

    @Nested
    class RecentlyUsedSubjects {

        List<Subject> subjects = new ArrayList<>();

        @Test
        @DisplayName("최근 사용한 문제집 목록을 조회 한다")
        void recentlyUsedSubjects() {
            // given
            Member jake = memberRepository.save(MemberFixture.jake());

            subjects = List.of(
                    Subject.builder().title("java 1").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 0)).build(),
                    Subject.builder().title("java 2").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 1)).build(),
                    Subject.builder().title("java 3").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 2)).build(),
                    Subject.builder().title("java 4").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 3)).build(),
                    Subject.builder().title("java 5").color("000000").author(jake).createdAt(LocalDateTime.of(2024, 1, 1, 0, 4)).build()
            );

            List<Question> questions = subjects.stream()
                    .limit(3)
                    .map(subject -> Question.builder().subject(subject).question(subject.getTitle() + " question").questionType(QuestionType.DESCRIPTIVE).build())
                    .collect(Collectors.toList());

            List<QuestionSubmitHistory> submitHistories = questions.stream()
                    .map(question -> QuestionSubmitHistory.builder().question(question).status(HistoryStatus.CORRECT).submittedAnswer("").createdBy(jake.getId()).build())
                    .collect(Collectors.toList());

            subjectRepository.saveAll(subjects);
            questionRepository.saveAll(questions);
            questionSubmitHistoryRepository.saveAll(submitHistories);

            // when
            List<SubjectListDto> recentlyUsedSubjects = subjectQueryRepository.getRecentlyUsedSubjects(jake.getId());

            // then
            assertThat(recentlyUsedSubjects)
                    .hasSize(3)
                    .extracting(SubjectListDto::title)
                    .containsExactlyInAnyOrder("java 1", "java 2", "java 3");
        }
    }


    @TestConfiguration
    static class Config {

        @Bean
        SubjectQueryRepository subjectQueryRepository(EntityManager entityManager) {
            return new SubjectQueryRepository(entityManager);
        }
    }

}
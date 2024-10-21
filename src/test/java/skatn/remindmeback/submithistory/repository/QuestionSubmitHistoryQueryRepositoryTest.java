package skatn.remindmeback.submithistory.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.subject.repository.SubjectRepository;

@DataJpaTest
class QuestionSubmitHistoryQueryRepositoryTest {

    @Autowired
    QuestionSubmitHistoryQueryRepository questionSubmitHistoryQueryRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    MemberRepository memberRepository;


    @TestConfiguration
    static class Config {
        @Bean
        QuestionSubmitHistoryQueryRepository questionSubmitHistoryQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
            return new QuestionSubmitHistoryQueryRepository(jdbcTemplate);
        }
    }

}
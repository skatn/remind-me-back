package skatn.remindmeback.question.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.scroll.ScrollRequest;
import skatn.remindmeback.question.repository.dto.QuestionScrollDto;

import java.util.List;

import static skatn.remindmeback.question.entity.QQuestion.question1;

@Repository
@Transactional(readOnly = true)
public class QuestionQueryRepository {

    private final JPAQueryFactory queryFactory;

    public QuestionQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Scroll<QuestionScrollDto> scrollQuestionList(long subjectId, ScrollRequest<Long, Long> scrollRequest) {
        List<QuestionScrollDto> questions = queryFactory.select(Projections.constructor(QuestionScrollDto.class,
                        question1.id,
                        question1.question,
                        question1.questionType
                ))
                .from(question1)
                .where(question1.subject.id.eq(subjectId),
                        questionIdGoe(scrollRequest.getCursor()))
                .limit(scrollRequest.getSize() + 1)
                .fetch();

        Long nextCursor = null;
        if(questions.size() > scrollRequest.getSize()) {
            nextCursor = questions.get(questions.size() - 1).id();
            questions.remove(questions.size() - 1);
        }

        return new Scroll<>(questions, nextCursor, null);
    }

    private BooleanExpression questionIdGoe(Long questionId) {
        return questionId == null ? null : question1.id.goe(questionId);
    }
}

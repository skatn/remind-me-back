package skatn.remindmeback.subject.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.scroll.ScrollRequest;
import skatn.remindmeback.common.scroll.ScrollUtils;
import skatn.remindmeback.subject.repository.dto.SubjectListDto;

import java.util.List;

import static skatn.remindmeback.question.entity.QQuestion.question1;
import static skatn.remindmeback.subject.entity.QSubject.subject;
import static skatn.remindmeback.submithistory.entity.QQuestionSubmitHistory.questionSubmitHistory;

@Repository
@Transactional(readOnly = true)
public class SubjectQueryRepository {

    private final JPAQueryFactory queryFactory;

    public SubjectQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Scroll<SubjectListDto> scrollSubjectList(long memberId, ScrollRequest<Long, Long> scrollRequest, String title) {
        List<SubjectListDto> subjects = queryFactory.select(Projections.constructor(SubjectListDto.class,
                        subject.id,
                        subject.title,
                        subject.color,
                        JPAExpressions.select(question1.id.count())
                                .from(question1)
                                .where(question1.subject.eq(subject))
                ))
                .from(subject)
                .where(subject.author.id.eq(memberId), subjectIdLoe(scrollRequest.getCursor()), titleContains(title))
                .orderBy(subject.id.desc())
                .limit(scrollRequest.getSize() + 1)
                .fetch();

        Long nextCursor = ScrollUtils.getNextCursor(subjects, scrollRequest.getSize(), SubjectListDto::id);

        return new Scroll<>(subjects, nextCursor, null);
    }

    public List<SubjectListDto> getRecentlyUsedSubjects(long memberId) {
        return queryFactory.select(Projections.constructor(SubjectListDto.class,
                        subject.id,
                        subject.title,
                        subject.color,
                        JPAExpressions.select(question1.id.count())
                                .from(question1)
                                .where(question1.subject.eq(subject))
                ))
                .from(subject)
                .join(question1).on(question1.subject.eq(subject))
                .join(questionSubmitHistory).on(questionSubmitHistory.question.eq(question1))
                .where(subject.author.id.eq(memberId))
                .orderBy(questionSubmitHistory.createdAt.desc())
                .groupBy(subject.id)
                .limit(10)
                .fetch();
    }

    private BooleanExpression subjectIdLoe(Long subjectId) {
        return subjectId == null ? null : subject.id.loe(subjectId);
    }

    private BooleanExpression titleContains(String title) {
        return title == null ? null : subject.title.contains(title);
    }
}

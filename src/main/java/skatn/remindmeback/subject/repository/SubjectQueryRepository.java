package skatn.remindmeback.subject.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.scroll.ScrollUtils;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.repository.dto.SubjectListDto;
import skatn.remindmeback.subject.repository.dto.SubjectListQueryCondition;

import java.util.List;
import java.util.Optional;

import static skatn.remindmeback.common.repository.Functions.groupConcat;
import static skatn.remindmeback.common.repository.Functions.groupConcatDistinct;
import static skatn.remindmeback.question.entity.QQuestion.question1;
import static skatn.remindmeback.subject.entity.QSubject.subject;
import static skatn.remindmeback.subject.entity.QSubjectTag.*;
import static skatn.remindmeback.subject.entity.QTag.*;
import static skatn.remindmeback.submithistory.entity.QQuestionSubmitHistory.questionSubmitHistory;

@Repository
@Transactional(readOnly = true)
public class SubjectQueryRepository {

    private final JPAQueryFactory queryFactory;

    public SubjectQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<Subject> findById(long subjectId) {
        Subject findSubject = queryFactory.selectFrom(subject)
                .leftJoin(subject.tags, subjectTag).fetchJoin()
                .leftJoin(subjectTag.tag, tag).fetchJoin()
                .where(subject.id.eq(subjectId))
                .fetchOne();

        return Optional.ofNullable(findSubject);
    }

    public Scroll<SubjectListDto> scrollSubjectList(long memberId, SubjectListQueryCondition condition) {
        List<SubjectListDto> subjects = queryFactory.select(Projections.constructor(SubjectListDto.class,
                        subject.id,
                        subject.title,
                        subject.color,
                        JPAExpressions.select(question1.id.count())
                                .from(question1)
                                .where(question1.subject.eq(subject)),
                        groupConcat(tag.name)
                ))
                .from(subject)
                .leftJoin(subject.tags, subjectTag).leftJoin(subjectTag.tag, tag)
                .where(subject.author.id.eq(memberId), subjectIdLoe(condition.scroll().getCursor()), titleContains(condition.title()))
                .groupBy(subject)
                .having(tagIn(condition.tags()))
                .orderBy(subject.id.desc())
                .limit(condition.scroll().getSize() + 1)
                .fetch();

        Long nextCursor = ScrollUtils.getNextCursor(subjects, condition.scroll().getSize(), SubjectListDto::id);

        return new Scroll<>(subjects, nextCursor, null);
    }

    public List<SubjectListDto> getRecentlyUsedSubjects(long memberId) {
        return queryFactory.select(Projections.constructor(SubjectListDto.class,
                        subject.id,
                        subject.title,
                        subject.color,
                        JPAExpressions.select(question1.id.count())
                                .from(question1)
                                .where(question1.subject.eq(subject)),
                        groupConcatDistinct(tag.name)
                ))
                .from(subject)
                .leftJoin(subject.tags, subjectTag).leftJoin(subjectTag.tag, tag)
                .join(question1).on(question1.subject.eq(subject))
                .join(questionSubmitHistory).on(questionSubmitHistory.question.eq(question1))
                .where(questionSubmitHistory.createdBy.eq(memberId))
                .orderBy(questionSubmitHistory.createdAt.max().desc())
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

    private BooleanExpression tagIn(List<String> tags) {
        if(tags == null || tags.isEmpty()) return null;
        return tag.name.in(tags).count().gt(0);
    }
}

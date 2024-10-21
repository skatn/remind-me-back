package skatn.remindmeback.subject.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.scroll.ScrollUtils;
import skatn.remindmeback.subject.entity.QSubject;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.repository.dto.SubjectListDto;
import skatn.remindmeback.subject.repository.dto.SubjectListQueryCondition;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static skatn.remindmeback.common.repository.Functions.groupConcat;
import static skatn.remindmeback.member.entity.QMember.member;
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

    public Scroll<SubjectListDto> scrollSubjectList(Long memberId, SubjectListQueryCondition condition) {
        List<SubjectListDto> subjects = queryFactory.select(Projections.constructor(SubjectListDto.class,
                        subject.id,
                        subject.title,
                        subject.color,
                        subject.createdAt,
                        subject.updatedAt,
                        selectQuestionCountSubQuery(subject),
                        selectTagsSubQuery(subject),
                        Projections.constructor(SubjectListDto.Author.class,
                                member.id,
                                member.name
                        )
                ))
                .from(subject)
                .join(subject.author, member)
                .where(
                        authorIdEq(memberId),
                        ExpressionUtils.or(
                                createdAtLt(condition.scroll().getCursor()),
                                ExpressionUtils.and(
                                        createdAtEq(condition.scroll().getCursor()),
                                        subjectIdGoe(condition.scroll().getSubCursor())
                                )
                        ),
                        subjectInFilteredByTag(condition.tags()),
                        titleContains(condition.title())
                )
                .orderBy(subject.createdAt.desc(), subject.id.asc())
                .limit(condition.scroll().getSize() + 1)
                .fetch();

        LocalDateTime nextCursor = ScrollUtils.getNextCursor(subjects, condition.scroll().getSize(), SubjectListDto::createdAt);
        Long nextSubCursor = ScrollUtils.getNextCursor(subjects, condition.scroll().getSize(), SubjectListDto::id);
        if (nextCursor != null) subjects.remove(subjects.size() - 1);

        return new Scroll<>(subjects, nextCursor, nextSubCursor);
    }

    public List<SubjectListDto> getRecentlyUsedSubjects(long memberId) {
        return queryFactory.select(Projections.constructor(SubjectListDto.class,
                        subject.id,
                        subject.title,
                        subject.color,
                        subject.createdAt,
                        subject.updatedAt,
                        selectQuestionCountSubQuery(subject),
                        selectTagsSubQuery(subject),
                        Projections.constructor(SubjectListDto.Author.class,
                                member.id,
                                member.name
                        )
                ))
                .from(subject)
                .join(subject.author, member)
                .join(question1).on(question1.subject.eq(subject))
                .join(questionSubmitHistory).on(questionSubmitHistory.question.eq(question1))
                .where(questionSubmitHistory.createdBy.eq(memberId))
                .groupBy(subject.id)
                .orderBy(questionSubmitHistory.createdAt.max().desc())
                .limit(10)
                .fetch();
    }

    private BooleanExpression titleContains(String title) {
        return title == null ? null : subject.title.contains(title);
    }

    private BooleanExpression createdAtLt(LocalDateTime createdAt) {
        return createdAt == null ? null : subject.createdAt.lt(createdAt);
    }

    private BooleanExpression createdAtEq(LocalDateTime createdAt) {
        return createdAt == null ? null : subject.createdAt.eq(createdAt);
    }

    private BooleanExpression subjectInFilteredByTag(List<String> tags) {
        if (tags == null || tags.isEmpty()) return null;

        return subject.in(
                JPAExpressions.select(subjectTag.subject)
                        .from(subjectTag)
                        .join(subjectTag.tag, tag)
                        .where(tag.name.in(tags))
        );
    }

    private BooleanExpression subjectIdGoe(Long subjectId) {
        return subjectId == null ? null : subject.id.goe(subjectId);
    }

    private BooleanExpression authorIdEq(Long authorId) {
        return authorId == null ? null : subject.author.id.eq(authorId);
    }

    private JPQLQuery<Long> selectQuestionCountSubQuery(QSubject subject) {
        if (subject == null) return null;

        return JPAExpressions.select(question1.id.count())
                .from(question1)
                .where(question1.subject.eq(subject));
    }

    private JPQLQuery<String> selectTagsSubQuery(QSubject subject) {
        if (subject == null) return null;

        return JPAExpressions.select(groupConcat(tag.name))
                .from(subjectTag)
                .join(subjectTag.tag, tag)
                .where(subjectTag.subject.eq(subject));
    }
}

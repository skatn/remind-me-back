package skatn.remindmeback.question.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.scroll.ScrollRequest;
import skatn.remindmeback.common.scroll.ScrollUtils;
import skatn.remindmeback.question.repository.dto.QuestionNotificationDto;
import skatn.remindmeback.question.repository.dto.QuestionScrollDto;

import java.time.LocalDateTime;
import java.util.List;

import static skatn.remindmeback.common.fcm.entity.QFcmToken.fcmToken;
import static skatn.remindmeback.member.entity.QMember.member;
import static skatn.remindmeback.question.entity.QQuestion.question1;
import static skatn.remindmeback.subject.entity.QSubject.subject;

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

        Long nextCursor = ScrollUtils.getNextCursor(questions, scrollRequest.getSize(), QuestionScrollDto::id);
        if(nextCursor != null) questions.remove(questions.size() - 1);

        return new Scroll<>(questions, nextCursor, null);
    }

    public List<QuestionNotificationDto> getQuestionsForNotification(LocalDateTime time) {
        return queryFactory.select(Projections.constructor(QuestionNotificationDto.class,
                        question1.id,
                        subject.id,
                        subject.title,
                        question1.question,
                        fcmToken.token
                ))
                .from(question1)
                .join(question1.subject, subject)
                .join(subject.author, member)
                .join(fcmToken).on(fcmToken.member.id.eq(member.id))
                .where(member.isActive.eq(true)
                        .and(subject.isEnableNotification.eq(true))
                        .and(question1.notificationTime.loe(time))
                )
                .fetch();
    }

    private BooleanExpression questionIdGoe(Long questionId) {
        return questionId == null ? null : question1.id.goe(questionId);
    }
}

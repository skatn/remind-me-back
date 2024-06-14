package skatn.remindmeback.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skatn.remindmeback.question.entity.Question;

import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Modifying
    @Query("update Question q set q.notificationTime = null where q.id in :questionIds")
    void clearNotificationTime(@Param("questionIds") Set<Long> questionIds);

}

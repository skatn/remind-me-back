package skatn.remindmeback.submithistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skatn.remindmeback.submithistory.entity.QuestionSubmitHistory;

public interface QuestionSubmitHistoryRepository extends JpaRepository<QuestionSubmitHistory, Long> {
}

package skatn.remindmeback.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skatn.remindmeback.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}

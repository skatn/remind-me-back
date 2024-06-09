package skatn.remindmeback.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skatn.remindmeback.subject.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}

package skatn.remindmeback.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skatn.remindmeback.subject.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}

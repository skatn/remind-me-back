package skatn.remindmeback.subject.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static skatn.remindmeback.subject.entity.QTag.tag;

@Repository
@Transactional(readOnly = true)
public class TagQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TagQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<String> findAll(int size, String keyword) {
        return queryFactory.select(tag.name)
                .from(tag)
                .where(tagNameContains(keyword))
                .limit(size)
                .fetch();
    }

    private BooleanExpression tagNameContains(String str) {
        return StringUtils.hasText(str) ? tag.name.contains(str) : null;
    }


}

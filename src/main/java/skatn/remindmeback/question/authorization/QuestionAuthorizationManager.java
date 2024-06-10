package skatn.remindmeback.question.authorization;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.security.dto.AccountDto;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionAuthorizationManager {

    private final EntityManager em;

    public boolean hasReadPermission(Authentication authentication, long questionId) {
        AccountDto accountDto = (AccountDto) authentication.getPrincipal();

        try {
            em.createQuery("""
                            select 1
                            from Question q
                            join q.subject s
                            where q.id = :questionId and s.author.id = :authorId
                            """, Integer.class)
                    .setParameter("questionId", questionId)
                    .setParameter("authorId", accountDto.id())
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean hasWritePermission(Authentication authentication, long questionId) {
        AccountDto accountDto = (AccountDto) authentication.getPrincipal();

        try {
            em.createQuery("""
                            select 1
                            from Question q
                            join q.subject s
                            where q.id = :questionId and s.author.id = :authorId
                            """, Integer.class)
                    .setParameter("questionId", questionId)
                    .setParameter("authorId", accountDto.id())
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean hasDeletePermission(Authentication authentication, long questionId) {
        AccountDto accountDto = (AccountDto) authentication.getPrincipal();

        try {
            em.createQuery("""
                            select 1
                            from Question q
                            join q.subject s
                            where q.id = :questionId and s.author.id = :authorId
                            """, Integer.class)
                    .setParameter("questionId", questionId)
                    .setParameter("authorId", accountDto.id())
                    .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}

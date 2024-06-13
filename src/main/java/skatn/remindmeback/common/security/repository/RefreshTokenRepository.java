package skatn.remindmeback.common.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skatn.remindmeback.common.security.entity.RefreshToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByParentToken(String parentToken);
    Optional<RefreshToken> findByToken(String token);
    void deleteByTokenGroup(String tokenGroup);
    void deleteByExpirationBefore(LocalDateTime dateTime);

    void deleteByMemberId(long memberId);
}

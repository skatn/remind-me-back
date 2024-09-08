package skatn.remindmeback.common.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skatn.remindmeback.common.fcm.entity.FcmToken;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByToken(String token);

    void deleteByRefreshTokenGroup(String refreshTokenGroup);
}

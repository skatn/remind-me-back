package skatn.remindmeback.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.exception.AuthException;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.common.fcm.repository.FcmTokenRepository;
import skatn.remindmeback.common.security.entity.RefreshToken;
import skatn.remindmeback.common.security.jwt.JwtProvider;
import skatn.remindmeback.common.security.jwt.TokenDto;
import skatn.remindmeback.common.security.repository.RefreshTokenRepository;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void save(String refreshToken, Long memberId) {
        LocalDateTime expiration = jwtProvider.getExpiration(refreshToken).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .expiration(expiration)
                .memberId(member.getId())
                .tokenGroup(UUID.randomUUID().toString())
                .build();

        refreshTokenRepository.save(token);
    }

    @Transactional(noRollbackFor = AuthException.class)
    public TokenDto reissue(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException(ErrorCode.JWT_INVALID_REFRESH_TOKEN));

        try {
            jwtProvider.validateToken(refreshToken);

            if (refreshTokenRepository.existsByParentToken(refreshToken)) {
                refreshTokenRepository.deleteByTokenGroup(findRefreshToken.getTokenGroup());
                throw new AuthException(ErrorCode.JWT_INVALID_REFRESH_TOKEN);
            }
        } catch (AuthException e) {
            fcmTokenRepository.deleteByRefreshTokenGroup(findRefreshToken.getTokenGroup());
            throw e;
        }

        Member member = memberRepository.findById(findRefreshToken.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        TokenDto tokenDto = jwtProvider.generateTokens(member.getId());
        LocalDateTime expiration = jwtProvider.getExpiration(tokenDto.refreshToken()).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshToken token = RefreshToken.builder()
                .parentToken(findRefreshToken.getToken())
                .token(tokenDto.refreshToken())
                .expiration(expiration)
                .memberId(findRefreshToken.getMemberId())
                .tokenGroup(findRefreshToken.getTokenGroup())
                .build();

        refreshTokenRepository.save(token);

        return tokenDto;
    }

    @Transactional
    public void deleteRefreshTokenGroup(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> refreshTokenRepository.deleteByTokenGroup(token.getTokenGroup()));
    }
}

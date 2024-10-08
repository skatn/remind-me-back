package skatn.remindmeback.common.fcm.service;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import skatn.remindmeback.common.exception.AuthException;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.common.exception.FirebaseException;
import skatn.remindmeback.common.fcm.dto.FcmDto;
import skatn.remindmeback.common.fcm.dto.FcmProperties;
import skatn.remindmeback.common.fcm.entity.FcmToken;
import skatn.remindmeback.common.fcm.repository.FcmTokenRepository;
import skatn.remindmeback.common.security.entity.RefreshToken;
import skatn.remindmeback.common.security.repository.RefreshTokenRepository;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmProperties fcmProperties;
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void send(Map<String, String> data, String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        FcmDto fcmDto = new FcmDto(false, new FcmDto.Message(data, token));
        HttpEntity<FcmDto> http = new HttpEntity<>(fcmDto, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(fcmProperties.sendApiUrl(), HttpMethod.POST, http, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new FirebaseException(ErrorCode.FAILED_SEND_FCM);
            }
        } catch (Exception e) {
            throw new FirebaseException(ErrorCode.FAILED_SEND_FCM, e);
        }
    }

    @Transactional
    public void addToken(long memberId, String token, String refreshToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if(fcmTokenRepository.findByToken(token).isPresent()) {
            return;
        }

        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException(ErrorCode.JWT_INVALID_REFRESH_TOKEN));

        FcmToken fcmToken = FcmToken.builder()
                .token(token)
                .member(member)
                .refreshTokenGroup(findRefreshToken.getTokenGroup())
                .build();

        fcmTokenRepository.save(fcmToken);
    }

    @Transactional
    public void deleteToken(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException(ErrorCode.JWT_INVALID_REFRESH_TOKEN));

        fcmTokenRepository.deleteByRefreshTokenGroup(findRefreshToken.getTokenGroup());
    }

    private String getAccessToken() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(fcmProperties.configPath()).getInputStream())
                    .createScoped(List.of(fcmProperties.authUrl()));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new FirebaseException(ErrorCode.FAILED_SEND_FCM, e);
        }
    }
}

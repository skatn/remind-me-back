package skatn.remindmeback.common.fcm.service;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.common.exception.FirebaseException;
import skatn.remindmeback.common.fcm.dto.FcmDto;
import skatn.remindmeback.common.fcm.dto.FcmProperties;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmProperties fcmProperties;

    public void send(String title, String body, String image, String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<FcmDto> http = new HttpEntity<>(createMessage(title, body, image, token), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(fcmProperties.sendApiUrl(), HttpMethod.POST, http, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new FirebaseException(ErrorCode.FAILED_SEND_FCM);
            }
        } catch (Exception e) {
            throw new FirebaseException(ErrorCode.FAILED_SEND_FCM, e);
        }
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

    private FcmDto createMessage(String title, String body, String image, String token) {
        return new FcmDto(
                false,
                new FcmDto.Message(
                        new FcmDto.Notification(
                                title,
                                body,
                                image
                        ),
                        token
                )
        );
    }
}

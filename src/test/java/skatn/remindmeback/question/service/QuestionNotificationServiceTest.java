package skatn.remindmeback.question.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skatn.remindmeback.common.fcm.service.FcmService;
import skatn.remindmeback.common.fixture.QuestionServiceFixture;
import skatn.remindmeback.question.repository.QuestionQueryRepository;
import skatn.remindmeback.question.repository.dto.QuestionNotificationDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class QuestionNotificationServiceTest {

    @InjectMocks
    QuestionNotificationService questionNotificationService;

    @Mock
    FcmService fcmService;
    @Mock
    QuestionQueryRepository questionQueryRepository;




    @Test
    @DisplayName("문제 풀이 FCM Push 알림을 발송한다")
    void notification() {
        // given
        List<QuestionNotificationDto> notificationDtos = QuestionServiceFixture.notificationDto();
        given(questionQueryRepository.getQuestionsForNotification(any()))
                .willReturn(notificationDtos);

        // when
        questionNotificationService.notification();

        // then
        then(fcmService).should(times(notificationDtos.size())).send(any(), any());
    }

}
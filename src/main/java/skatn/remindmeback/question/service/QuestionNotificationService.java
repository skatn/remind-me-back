package skatn.remindmeback.question.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.exception.FirebaseException;
import skatn.remindmeback.common.fcm.service.FcmService;
import skatn.remindmeback.question.repository.QuestionQueryRepository;
import skatn.remindmeback.question.repository.QuestionRepository;
import skatn.remindmeback.question.repository.dto.QuestionNotificationDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionNotificationService {

    private final FcmService fcmService;
    private final QuestionRepository questionRepository;
    private final QuestionQueryRepository questionQueryRepository;

    @Scheduled(cron = "0 * * * * *")
    public void notification() {
        List<QuestionNotificationDto> questions = questionQueryRepository.getQuestionsForNotification(LocalDateTime.now());
        int failCount = 0;

        for (QuestionNotificationDto question : questions) {
            try {
                HashMap<String, String> data = new HashMap<>() {{
                    put("title", question.title());
                    put("body", question.body());
                    put("subjectId", String.valueOf(question.subjectId()));
                    put("questionId", String.valueOf(question.questionId()));
                }};
                fcmService.send(data, question.token());
            } catch (FirebaseException e) {
                log.error("FCM 발송 실패 [{}]", question, e);
                failCount++;
            }
        }

        if (failCount > 0) {
            log.warn("FCM 발송 총 {}건 중 {}건 성공, {}건 실패", questions.size(), questions.size() - failCount, failCount);
        }

        Set<Long> questionIds = questions.stream()
                .map(QuestionNotificationDto::questionId)
                .collect(Collectors.toSet());

        if (!questionIds.isEmpty()) {
            questionRepository.clearNotificationTime(questionIds);
        }
    }
}

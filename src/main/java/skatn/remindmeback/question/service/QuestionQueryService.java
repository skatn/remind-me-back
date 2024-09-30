package skatn.remindmeback.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.question.controller.dto.QuestionScrollRequest;
import skatn.remindmeback.question.dto.QuestionDto;
import skatn.remindmeback.question.entity.Question;
import skatn.remindmeback.question.repository.QuestionQueryRepository;
import skatn.remindmeback.question.repository.QuestionRepository;
import skatn.remindmeback.question.repository.dto.QuestionScrollDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionQueryService {

    private final QuestionRepository questionRepository;
    private final QuestionQueryRepository questionQueryRepository;

    @PreAuthorize("@questionAuthorizationManager.hasReadPermission(authentication, #questionId)")
    public QuestionDto getQuestion(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.QUESTION_NOT_FOUND));

        return new QuestionDto(question);
    }

    public Scroll<QuestionScrollDto> getQuestionList(QuestionScrollRequest request) {
        return questionQueryRepository.scrollQuestionList(request.getSubjectId(), request);
    }
}

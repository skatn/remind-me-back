package skatn.remindmeback.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.security.annotation.AuthUser;
import skatn.remindmeback.common.security.dto.AccountDto;
import skatn.remindmeback.question.controller.dto.*;
import skatn.remindmeback.question.dto.QuestionDto;
import skatn.remindmeback.question.repository.dto.QuestionScrollDto;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.service.QuestionCommandService;
import skatn.remindmeback.question.service.QuestionQueryService;
import skatn.remindmeback.submithistory.QuestionSubmitHistoryQueryService;
import skatn.remindmeback.submithistory.repository.dto.QuestionSubmitHistoryCountDto;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private final QuestionSubmitHistoryQueryService questionSubmitHistoryQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionCreateResponse create(@Valid @RequestBody QuestionCreateRequest request,
                                         BindingResult bindingResult) throws BindException {

        validateCreateRequest(request, bindingResult);

        long questionId = questionCommandService.create(request.toQuestionCreateDto());
        return new QuestionCreateResponse(questionId);
    }

    @GetMapping("/{questionId}")
    public QuestionDto get(@PathVariable("questionId") long questionId) {
        return questionQueryService.getQuestion(questionId);
    }

    @PatchMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("questionId") long questionId,
                       @Valid @RequestBody QuestionUpdateRequest request,
                       BindingResult bindingResult) throws BindException {

        validateUpdateRequest(request, bindingResult);

        questionCommandService.update(request.toQuestionUpdateDto(questionId));
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("questionId") long questionId) {
        questionCommandService.delete(questionId);
    }

    @GetMapping
    @PreAuthorize("@subjectAuthorizationManager.hasReadPermission(authentication, #request.subjectId)")
    public Scroll<QuestionScrollDto> scrollQuestionList(@Valid @ModelAttribute QuestionScrollRequest request) {
        return questionQueryService.getQuestionList(request);
    }

    @PostMapping("/submit")
    public QuestionMarkingResponse submit(@Valid @RequestBody QuestionMarkingRequest request) {
        boolean correct = questionCommandService.submit(request.questionId(), request.submittedAnswer());
        return new QuestionMarkingResponse(correct);
    }

    @GetMapping("/histories/{year}")
    public Map<String, List<QuestionSubmitHistoryCountDto>> getDailyWithinYear(@AuthUser AccountDto accountDto, @PathVariable("year") int year) {
        return questionSubmitHistoryQueryService.getDailyWithinYear(accountDto.id(), year);
    }

    @GetMapping("/histories/last-30-days")
    public List<QuestionSubmitHistoryCountDto> getLast30Days(@AuthUser AccountDto accountDto) {
        return questionSubmitHistoryQueryService.getLast30Days(accountDto.id());
    }

    private void validateCreateRequest(QuestionCreateRequest request, BindingResult bindingResult) throws BindException {
        if (request.questionType() == QuestionType.CHOICE) {
            if(request.answers().stream()
                    .filter(QuestionCreateRequest.AnswerDto::isAnswer)
                    .count() != 1L)
                bindingResult.reject("invalidChoiceAnswer", "답변의 정답은 1개만 있어야 합니다.");

            if(request.answers().size() < 3 || request.answers().size() > 5)
                bindingResult.reject("invalidChoiceAnswerCount", "답변은 3 ~ 5개 까지 작성할 수 있습니다.");
        }
        else if (request.questionType() == QuestionType.DESCRIPTIVE && request.answers().size() != 1) {
            bindingResult.reject("invalidDescriptiveAnswer", "답변은 1개만 있어야 합니다.");
        }

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }

    private void validateUpdateRequest(QuestionUpdateRequest request, BindingResult bindingResult) throws BindException {
        if (request.questionType() == QuestionType.CHOICE && request.answers().stream()
                .filter(QuestionUpdateRequest.AnswerDto::isAnswer)
                .count() != 1L) {

            bindingResult.reject("invalidChoiceAnswer", "답변의 정답은 1개만 있어야 합니다.");
        }
        else if (request.questionType() == QuestionType.DESCRIPTIVE && request.answers().size() != 1) {
            bindingResult.reject("invalidDescriptiveAnswer", "답변은 1개만 있어야 합니다.");
        }

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }
}

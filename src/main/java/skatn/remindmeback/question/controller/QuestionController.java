package skatn.remindmeback.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.question.controller.dto.*;
import skatn.remindmeback.question.dto.QuestionDto;
import skatn.remindmeback.question.repository.QuestionQueryRepository;
import skatn.remindmeback.question.repository.dto.QuestionScrollDto;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.service.QuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionQueryRepository questionQueryRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionCreateResponse create(@Valid @RequestBody QuestionCreateRequest request,
                                         BindingResult bindingResult) throws BindException {

        validateCreateRequest(request, bindingResult);

        long questionId = questionService.create(request.toQuestionCreateDto());
        return new QuestionCreateResponse(questionId);
    }

    @GetMapping("/{questionId}")
    public QuestionDto get(@PathVariable("questionId") long questionId) {
        return questionService.findOne(questionId);
    }

    @PatchMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("questionId") long questionId,
                       @Valid @RequestBody QuestionUpdateRequest request,
                       BindingResult bindingResult) throws BindException {

        validateUpdateRequest(request, bindingResult);

        questionService.update(request.toQuestionUpdateDto(questionId));
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("questionId") long questionId) {
        questionService.delete(questionId);
    }

    @GetMapping
    @PreAuthorize("@subjectAuthorizationManager.hasReadPermission(authentication, #request.subjectId)")
    public Scroll<QuestionScrollDto> scrollQuestionList(@Valid @ModelAttribute QuestionScrollRequest request) {
        return questionQueryRepository.scrollQuestionList(request.getSubjectId(), request);
    }

    @PostMapping("/submit")
    public QuestionMarkingResponse submit(@Valid @RequestBody QuestionMarkingRequest request) {
        boolean correct = questionService.submit(request.questionId(), request.submittedAnswer());
        return new QuestionMarkingResponse(correct);
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

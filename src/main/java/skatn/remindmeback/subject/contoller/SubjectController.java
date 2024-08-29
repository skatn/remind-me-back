package skatn.remindmeback.subject.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.security.annotation.AuthUser;
import skatn.remindmeback.common.security.dto.AccountDto;
import skatn.remindmeback.subject.contoller.dto.*;
import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.repository.SubjectQueryRepository;
import skatn.remindmeback.subject.repository.dto.SubjectListDto;
import skatn.remindmeback.subject.service.SubjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectQueryRepository subjectQueryRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectCreateResponse create(@AuthUser AccountDto accountDto, @Valid @RequestBody SubjectCreateRequest request) {
        Long subjectId = subjectService.create(accountDto.id(), request.title(), request.color());
        return new SubjectCreateResponse(subjectId);
    }

    @GetMapping("/{subjectId}")
    public SubjectDto get(@PathVariable("subjectId") long subjectId) {
        return subjectService.findOne(subjectId);
    }

    @PatchMapping("/{subjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("subjectId") long subjectId, @Valid @RequestBody SubjectUpdateRequest request) {
        subjectService.update(subjectId, request.title(), request.color());
    }

    @GetMapping("/{subjectId}/notification")
    public SubjectNotificationResponse getNotificationStatus(@PathVariable("subjectId") long subjectId) {
        boolean isEnable = subjectService.getNotificationStatus(subjectId);
        return new SubjectNotificationResponse(isEnable);
    }

    @PatchMapping("/{subjectId}/notification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNotification(@PathVariable("subjectId") long subjectId, @Valid @RequestBody SubjectNotificationUpdateRequest request) {
        subjectService.updateNotification(subjectId, request.enable());
    }

    @DeleteMapping("/{subjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("subjectId") long subjectId) {
        subjectService.delete(subjectId);
    }

    @GetMapping
    public Scroll<SubjectListDto> scrollSubjectList(@AuthUser AccountDto accountDto, @Valid @ModelAttribute SubjectScrollRequest request) {
        return subjectQueryRepository.scrollSubjectList(accountDto.id(), request, request.getTitle());
    }

    @GetMapping("/recent")
    public List<SubjectListDto> getRecentlyUsedSubjects(@AuthUser AccountDto accountDto) {
        return subjectQueryRepository.getRecentlyUsedSubjects(accountDto.id());
    }

}

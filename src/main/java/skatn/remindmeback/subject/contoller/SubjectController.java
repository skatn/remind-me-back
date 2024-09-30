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
import skatn.remindmeback.subject.repository.dto.SubjectListDto;
import skatn.remindmeback.subject.service.SubjectCommandService;
import skatn.remindmeback.subject.service.SubjectQueryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectCommandService subjectCommandService;
    private final SubjectQueryService subjectQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectCreateResponse create(@AuthUser AccountDto accountDto, @Valid @RequestBody SubjectCreateRequest request) {
        Long subjectId = subjectCommandService.create(accountDto.id(), request.title(), request.color());
        return new SubjectCreateResponse(subjectId);
    }

    @GetMapping("/{subjectId}")
    public SubjectDto get(@PathVariable("subjectId") long subjectId) {
        return subjectQueryService.getSubject(subjectId);
    }

    @PatchMapping("/{subjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("subjectId") long subjectId, @Valid @RequestBody SubjectUpdateRequest request) {
        subjectCommandService.update(subjectId, request.title(), request.color());
    }

    @GetMapping("/{subjectId}/notification")
    public SubjectNotificationResponse getNotificationStatus(@PathVariable("subjectId") long subjectId) {
        boolean isEnable = subjectCommandService.getNotificationStatus(subjectId);
        return new SubjectNotificationResponse(isEnable);
    }

    @PatchMapping("/{subjectId}/notification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNotification(@PathVariable("subjectId") long subjectId, @Valid @RequestBody SubjectNotificationUpdateRequest request) {
        subjectCommandService.updateNotification(subjectId, request.enable());
    }

    @DeleteMapping("/{subjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("subjectId") long subjectId) {
        subjectCommandService.delete(subjectId);
    }

    @GetMapping
    public Scroll<SubjectListDto> scrollSubjectList(@AuthUser AccountDto accountDto, @Valid @ModelAttribute SubjectScrollRequest request) {
        return subjectQueryService.getSubjectList(accountDto.id(), request, request.getTitle());
    }

    @GetMapping("/recent")
    public List<SubjectListDto> getRecentlyUsedSubjects(@AuthUser AccountDto accountDto) {
        return subjectQueryService.getRecentlyUsedSubjects(accountDto.id());
    }

}

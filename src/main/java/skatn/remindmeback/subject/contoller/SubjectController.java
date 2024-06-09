package skatn.remindmeback.subject.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import skatn.remindmeback.common.security.annotation.AuthUser;
import skatn.remindmeback.common.security.dto.AccountDto;
import skatn.remindmeback.subject.contoller.dto.SubjectCreateRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectCreateResponse;
import skatn.remindmeback.subject.contoller.dto.SubjectNotificationUpdateRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectUpdateRequest;
import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.service.SubjectService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subject")
public class SubjectController {

    private final SubjectService subjectService;

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

}

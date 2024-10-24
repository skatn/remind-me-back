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
import skatn.remindmeback.subject.repository.dto.SubjectListQueryCondition;
import skatn.remindmeback.subject.service.SubjectCommandService;
import skatn.remindmeback.subject.service.SubjectQueryService;
import skatn.remindmeback.subject.service.dto.SubjectCreateDto;
import skatn.remindmeback.subject.service.dto.SubjectUpdateDto;

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
        Long subjectId = subjectCommandService.create(new SubjectCreateDto(accountDto.id(), request.title(), request.color(), request.visibility(), request.tags()));
        return new SubjectCreateResponse(subjectId);
    }

    @GetMapping("/{subjectId}")
    public SubjectDto get(@PathVariable("subjectId") long subjectId) {
        return subjectQueryService.getSubject(subjectId);
    }

    @PatchMapping("/{subjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("subjectId") long subjectId, @Valid @RequestBody SubjectUpdateRequest request) {
        subjectCommandService.update(new SubjectUpdateDto(subjectId, request.title(), request.color(), request.isEnableNotification(), request.visibility(), request.tags()));
    }

    @DeleteMapping("/{subjectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("subjectId") long subjectId) {
        subjectCommandService.delete(subjectId);
    }

    @GetMapping
    public Scroll<SubjectListDto> scrollSubjectList(@AuthUser AccountDto accountDto, @Valid @ModelAttribute SubjectScrollRequest request) {
        SubjectListQueryCondition condition = new SubjectListQueryCondition(request, request.getTitle(), request.getTags());
        return subjectQueryService.getSubjectList(accountDto.id(), condition);
    }

    @GetMapping("/recent")
    public List<SubjectListDto> getRecentlyUsedSubjects(@AuthUser AccountDto accountDto) {
        return subjectQueryService.getRecentlyUsedSubjects(accountDto.id());
    }

}

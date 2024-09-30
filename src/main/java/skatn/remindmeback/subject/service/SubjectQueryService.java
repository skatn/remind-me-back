package skatn.remindmeback.subject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.repository.SubjectQueryRepository;
import skatn.remindmeback.subject.repository.dto.SubjectListDto;
import skatn.remindmeback.subject.repository.dto.SubjectListQueryCondition;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectQueryService {

    private final SubjectQueryRepository subjectQueryRepository;

    @PreAuthorize("@subjectAuthorizationManager.hasReadPermission(authentication, #subjectId)")
    public SubjectDto getSubject(long subjectId) {
        Subject subject = subjectQueryRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUBJECT_NOT_FOUND));

        return new SubjectDto(subject);
    }

    public Scroll<SubjectListDto> getSubjectList(long memberId, SubjectListQueryCondition condition) {
        return subjectQueryRepository.scrollSubjectList(memberId, condition);
    }

    public List<SubjectListDto> getRecentlyUsedSubjects(long memberId) {
        return subjectQueryRepository.getRecentlyUsedSubjects(memberId);
    }
}

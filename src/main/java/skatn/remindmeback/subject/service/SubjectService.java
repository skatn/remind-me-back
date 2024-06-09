package skatn.remindmeback.subject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.repository.SubjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectService {

    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public Long create(long authorId, String title, String color) {
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return subjectRepository.save(Subject.builder()
                .author(author)
                .title(title)
                .color(color)
                .build()).getId();
    }

    public SubjectDto findOne(long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUBJECT_NOT_FOUND));

        return new SubjectDto(subject);
    }

    @Transactional
    @PreAuthorize("@subjectAuthorizationManager.hasWritePermission(authentication, #subjectId)")
    public void update(long subjectId, String title, String color) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUBJECT_NOT_FOUND));

        subject.changeTitle(title);
        subject.changeColor(color);
    }

    @Transactional
    @PreAuthorize("@subjectAuthorizationManager.hasDeletePermission(authentication, #subjectId)")
    public void delete(long subjectId) {
        subjectRepository.findById(subjectId)
                .ifPresent(subjectRepository::delete);
    }

    @Transactional
    @PreAuthorize("@subjectAuthorizationManager.hasWritePermission(authentication, #subjectId)")
    public void updateNotification(long subjectId, boolean enable) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUBJECT_NOT_FOUND));

        subject.changeEnableNotification(enable);
    }


}

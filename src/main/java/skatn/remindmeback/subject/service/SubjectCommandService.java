package skatn.remindmeback.subject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.subject.entity.Subject;
import skatn.remindmeback.subject.entity.Tag;
import skatn.remindmeback.subject.repository.SubjectRepository;
import skatn.remindmeback.subject.repository.TagRepository;
import skatn.remindmeback.subject.service.dto.SubjectCreateDto;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectCommandService {

    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    private final TagRepository tagRepository;

    @Transactional
    public long create(SubjectCreateDto subjectCreateDto) {
        Member author = memberRepository.findById(subjectCreateDto.authorId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));


        Subject subject = subjectRepository.save(Subject.builder()
                .author(author)
                .title(subjectCreateDto.title())
                .color(subjectCreateDto.color())
                .visibility(subjectCreateDto.visibility())
                .build());

        if(subjectCreateDto.tags() != null) {
            List<Tag> findTags = subjectCreateDto.tags().stream()
                    .map(tag -> tagRepository.findByName(tag).orElseGet(() -> tagRepository.save(Tag.builder().name(tag).build())))
                    .toList();

            subject.changeTags(findTags);
        }

        return subject.getId();
    }


    @Transactional
    @PreAuthorize("@subjectAuthorizationManager.hasWritePermission(authentication, #subjectId)")
    public void update(long subjectId, String title, String color, List<String> tags) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUBJECT_NOT_FOUND));

        subject.changeTitle(title);
        subject.changeColor(color);

        if(tags != null) {
            List<Tag> findTags = tags.stream()
                    .map(tag -> tagRepository.findByName(tag).orElseGet(() -> tagRepository.save(Tag.builder().name(tag).build())))
                    .toList();

            subject.changeTags(findTags);
        }
    }

    @Transactional
    @PreAuthorize("@subjectAuthorizationManager.hasDeletePermission(authentication, #subjectId)")
    public void delete(long subjectId) {
        subjectRepository.findById(subjectId)
                .ifPresent(subjectRepository::delete);
    }

    @PreAuthorize("@subjectAuthorizationManager.hasReadPermission(authentication, #subjectId)")
    public boolean getNotificationStatus(long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUBJECT_NOT_FOUND));

        return subject.isEnableNotification();
    }

    @Transactional
    @PreAuthorize("@subjectAuthorizationManager.hasWritePermission(authentication, #subjectId)")
    public void updateNotification(long subjectId, boolean enable) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUBJECT_NOT_FOUND));

        subject.changeEnableNotification(enable);
    }

}

package skatn.remindmeback.common.fixture;

import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.subject.contoller.dto.SubjectCreateRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectNotificationUpdateRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectScrollRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectUpdateRequest;
import skatn.remindmeback.subject.entity.Visibility;
import skatn.remindmeback.subject.repository.dto.SubjectListDto;

import java.time.LocalDateTime;
import java.util.List;

public class SubjectControllerFixture {

    public static SubjectCreateRequest createJavaRequest() {
        return new SubjectCreateRequest("java", "000000", Visibility.PUBLIC, List.of("java", "programming"));
    }

    public static SubjectUpdateRequest updateJavaRequest() {
        return new SubjectUpdateRequest("newJava", "FFFFFF", Visibility.PUBLIC, List.of("java", "programming"));
    }

    public static SubjectNotificationUpdateRequest notificationUpdateRequest() {
        return new SubjectNotificationUpdateRequest(true);
    }

    public static SubjectScrollRequest scrollRequest() {
        return SubjectScrollRequest.builder().build();
    }

    public static Scroll<SubjectListDto> scrollResponse() {
        return new Scroll<>(List.of(
            new SubjectListDto(1L, "subject 1", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 2", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 3", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 4", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 5", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 6", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 7", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 8", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 9", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake")),
                new SubjectListDto(1L, "subject 10", "FFFFFF", LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0), 10, List.of("tag1", "tag2"), new SubjectListDto.Author(1L, "jake"))
        ), null, null);
    }

}


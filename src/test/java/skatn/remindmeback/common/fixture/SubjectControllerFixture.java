package skatn.remindmeback.common.fixture;

import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.subject.contoller.dto.SubjectCreateRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectNotificationUpdateRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectScrollRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectUpdateRequest;
import skatn.remindmeback.subject.repository.dto.SubjectScrollDto;

import java.util.List;

public class SubjectControllerFixture {

    public static SubjectCreateRequest createRequest() {
        return new SubjectCreateRequest("title", "000000");
    }

    public static SubjectUpdateRequest updateRequest() {
        return new SubjectUpdateRequest("newTitle", "FFFFFF");
    }

    public static SubjectNotificationUpdateRequest notificationUpdateRequest() {
        return new SubjectNotificationUpdateRequest(true);
    }

    public static SubjectScrollRequest scrollRequest() {
        return new SubjectScrollRequest("");
    }

    public static Scroll<SubjectScrollDto> scrollResponse() {
        return new Scroll<>(List.of(
            new SubjectScrollDto(1L, "subject 1", "FFFFFF", 10),
            new SubjectScrollDto(2L, "subject 2", "FFFFFF", 10),
            new SubjectScrollDto(3L, "subject 3", "FFFFFF", 10),
            new SubjectScrollDto(4L, "subject 4", "FFFFFF", 10),
            new SubjectScrollDto(5L, "subject 5", "FFFFFF", 10),
            new SubjectScrollDto(6L, "subject 6", "FFFFFF", 10),
            new SubjectScrollDto(7L, "subject 7", "FFFFFF", 10),
            new SubjectScrollDto(8L, "subject 8", "FFFFFF", 10),
            new SubjectScrollDto(9L, "subject 9", "FFFFFF", 10),
            new SubjectScrollDto(10L, "subject 10", "FFFFFF", 10)
        ), null, null);
    }

}


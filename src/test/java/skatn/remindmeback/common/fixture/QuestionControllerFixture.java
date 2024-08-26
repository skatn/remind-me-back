package skatn.remindmeback.common.fixture;

import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.question.controller.dto.QuestionCreateRequest;
import skatn.remindmeback.question.controller.dto.QuestionMarkingRequest;
import skatn.remindmeback.question.controller.dto.QuestionScrollRequest;
import skatn.remindmeback.question.controller.dto.QuestionUpdateRequest;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.repository.dto.QuestionScrollDto;
import skatn.remindmeback.submithistory.repository.dto.QuestionSubmitHistoryCountDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuestionControllerFixture {

    public static QuestionCreateRequest createRequest() {
        return new QuestionCreateRequest(
                1L,
                "question",
                "image.png",
                QuestionType.CHOICE,
                "explanation",
                Set.of(
                        new QuestionCreateRequest.AnswerDto("answer 1", true),
                        new QuestionCreateRequest.AnswerDto("answer 2", false),
                        new QuestionCreateRequest.AnswerDto("answer 3", false)
                )
        );
    }


    public static QuestionCreateRequest createRequest(QuestionType questionType, Set<QuestionCreateRequest.AnswerDto> answers) {
        return new QuestionCreateRequest(
                1L,
                "question",
                "image.png",
                questionType,
                "explanation",
                answers
        );
    }

    public static QuestionUpdateRequest updateRequest() {
        return new QuestionUpdateRequest(
                "update question",
                "update_image.png",
                QuestionType.CHOICE,
                "update explanation",
                Set.of(
                        new QuestionUpdateRequest.AnswerDto("update answer 1", true),
                        new QuestionUpdateRequest.AnswerDto("update answer 2", false),
                        new QuestionUpdateRequest.AnswerDto("update answer 3", false)
                )
        );
    }


    public static QuestionUpdateRequest updateRequest(QuestionType questionType, Set<QuestionUpdateRequest.AnswerDto> answers) {
        return new QuestionUpdateRequest(
                "update question",
                "update_image.png",
                questionType,
                "update explanation",
                answers
        );
    }

    public static QuestionScrollRequest scrollRequest() {
        return new QuestionScrollRequest(1L);
    }

    public static Scroll<QuestionScrollDto> scrollResponse() {
        return new Scroll<>(List.of(
                new QuestionScrollDto(1L, "question 1", QuestionType.CHOICE),
                new QuestionScrollDto(2L, "question 2", QuestionType.CHOICE),
                new QuestionScrollDto(3L, "question 3", QuestionType.CHOICE),
                new QuestionScrollDto(4L, "question 4", QuestionType.CHOICE),
                new QuestionScrollDto(5L, "question 5", QuestionType.CHOICE),
                new QuestionScrollDto(6L, "question 6", QuestionType.CHOICE),
                new QuestionScrollDto(7L, "question 7", QuestionType.CHOICE),
                new QuestionScrollDto(8L, "question 8", QuestionType.CHOICE),
                new QuestionScrollDto(9L, "question 9", QuestionType.CHOICE),
                new QuestionScrollDto(10L, "question 10", QuestionType.CHOICE)
        ), null, null);
    }

    public static QuestionMarkingRequest markingRequest() {
        return new QuestionMarkingRequest(1L, "answer");
    }

    public static Map<String, List<QuestionSubmitHistoryCountDto>> withInYearResponse() {
        return new HashMap<>() {{
            put("2024-01", List.of(
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 1, 1), 2),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 1, 2), 0),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 1, 3), 10),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 1, 4), 23)
            ));
            put("2024-02", List.of(
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 2, 1), 2),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 2, 2), 0),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 2, 3), 10),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 2, 4), 23)
            ));
            put("2024-12", List.of(
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 12, 1), 2),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 12, 2), 0),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 12, 3), 10),
                    new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 12, 4), 23)
            ));
        }};
    }

    public static List<QuestionSubmitHistoryCountDto> last30DaysResponse() {
        return List.of(
                new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 1, 1), 2),
                new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 1, 2), 0),
                new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 1, 3), 10),
                new QuestionSubmitHistoryCountDto(LocalDate.of(2024, 1, 4), 23)
        );
    }
}

package skatn.remindmeback.question.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import skatn.remindmeback.common.controller.ControllerTest;
import skatn.remindmeback.common.fixture.QuestionControllerFixture;
import skatn.remindmeback.common.fixture.QuestionServiceFixture;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.security.WithRestMockUser;
import skatn.remindmeback.question.controller.dto.QuestionCreateRequest;
import skatn.remindmeback.question.controller.dto.QuestionMarkingRequest;
import skatn.remindmeback.question.controller.dto.QuestionScrollRequest;
import skatn.remindmeback.question.controller.dto.QuestionUpdateRequest;
import skatn.remindmeback.question.dto.QuestionDto;
import skatn.remindmeback.question.entity.QuestionType;
import skatn.remindmeback.question.repository.QuestionQueryRepository;
import skatn.remindmeback.question.repository.dto.QuestionScrollDto;
import skatn.remindmeback.question.service.QuestionCommandService;
import skatn.remindmeback.submithistory.repository.QuestionSubmitHistoryQueryRepository;
import skatn.remindmeback.submithistory.repository.dto.QuestionSubmitHistoryCountDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest extends ControllerTest {

    @MockBean
    QuestionCommandService questionCommandService;
    @MockBean
    QuestionQueryRepository questionQueryRepository;
    @MockBean
    QuestionSubmitHistoryQueryRepository questionSubmitHistoryQueryRepository;

    @Test
    @DisplayName("문제를 생성한다")
    void create() throws Exception {
        // given
        QuestionCreateRequest request = QuestionControllerFixture.createRequest();
        given(questionCommandService.create(any())).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.questionId").value(1));
    }

    @Test
    @DisplayName("문제를 생성할 때 객관식일 경우 답변의 정답이 2개 이상일 경우 실패한다")
    void createFailManyAnswersWhenChoice() throws Exception {
        // given
        QuestionCreateRequest request = QuestionControllerFixture.createRequest(QuestionType.CHOICE, Set.of(
                new QuestionCreateRequest.AnswerDto("answer 1", true),
                new QuestionCreateRequest.AnswerDto("answer 2", true),
                new QuestionCreateRequest.AnswerDto("answer 3", false)
        ));

        // when
        ResultActions result = mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제를 생성할 때 객관식일 경우 답변의 갯수는 3~5개 여야 한다")
    void createFailInvalidSizeWhenChoice() throws Exception {
        // given
        QuestionCreateRequest request = QuestionControllerFixture.createRequest(QuestionType.CHOICE, Set.of(
                new QuestionCreateRequest.AnswerDto("answer 1", true),
                new QuestionCreateRequest.AnswerDto("answer 2", false)
        ));

        // when
        ResultActions result = mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제를 생성할 때 서술형일 경우 답변의 갯수는 1개 여야 한다")
    void createFailInvalidSizeWhenDescriptive() throws Exception {
        // given
        QuestionCreateRequest request = QuestionControllerFixture.createRequest(QuestionType.DESCRIPTIVE, Set.of(
                new QuestionCreateRequest.AnswerDto("answer 1", true),
                new QuestionCreateRequest.AnswerDto("answer 2", false)
        ));

        // when
        ResultActions result = mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제를 단건 조회한다")
    void getQuestion() throws Exception {
        // given
        QuestionDto question = new QuestionDto(QuestionServiceFixture.question());
        given(questionCommandService.findOne(question.id())).willReturn(question);

        // when
        ResultActions result = mockMvc.perform(get("/api/questions/{questionId}", question.id()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(question.id()))
                .andExpect(jsonPath("$.questionType").value(question.questionType().toString()))
                .andExpect(jsonPath("$.questionImage").value(question.questionImage()))
                .andExpect(jsonPath("$.explanation").value(question.explanation()))
                .andExpect(jsonPath("$.answers.length()").value(question.answers().size()));
    }

    @Test
    @DisplayName("문제를 수정한다")
    void update() throws Exception {
        // given
        QuestionUpdateRequest request = QuestionControllerFixture.updateRequest();
        doNothing().when(questionCommandService).update(any());

        // when
        ResultActions result = mockMvc.perform(patch("/api/questions/{questionId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("문제를 수정할 때 객관식일 경우 답변의 정답이 2개 이상일 경우 실패한다")
    void updateFailManyAnswersWhenChoice() throws Exception {
        // given
        QuestionUpdateRequest request = QuestionControllerFixture.updateRequest(QuestionType.CHOICE, Set.of(
                new QuestionUpdateRequest.AnswerDto("answer 1", true),
                new QuestionUpdateRequest.AnswerDto("answer 2", true),
                new QuestionUpdateRequest.AnswerDto("answer 3", false)
        ));

        // when
        ResultActions result = mockMvc.perform(patch("/api/questions/{questionId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제를 수정할 때 서술형일 경우 답변의 갯수는 1개 여야 한다")
    void updateFailInvalidSizeWhenDescriptive() throws Exception {
        // given
        QuestionUpdateRequest request = QuestionControllerFixture.updateRequest(QuestionType.DESCRIPTIVE, Set.of(
                new QuestionUpdateRequest.AnswerDto("answer 1", true),
                new QuestionUpdateRequest.AnswerDto("answer 2", false)
        ));

        // when
        ResultActions result = mockMvc.perform(patch("/api/questions/{questionId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문제를 삭제한다")
    void deleteQuestion() throws Exception {
        // given
        doNothing().when(questionCommandService).delete(anyLong());

        // when
        ResultActions result = mockMvc.perform(delete("/api/questions/{questionId}", 1));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("문제 목록을 무한스크롤로 조회한다")
    void scrollQuestionList() throws Exception {
        // given
        QuestionScrollRequest request = QuestionControllerFixture.scrollRequest();
        Scroll<QuestionScrollDto> response = QuestionControllerFixture.scrollResponse();
        given(questionQueryRepository.scrollQuestionList(anyLong(), any()))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/api/questions")
                .param("subjectId", String.valueOf(request.getSubjectId()))
                .param("size", String.valueOf(request.getSize())));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(response.content().size()))
                .andExpect(jsonPath("$.content[0].id").value(response.content().get(0).id()))
                .andExpect(jsonPath("$.content[0].question").value(response.content().get(0).question()))
                .andExpect(jsonPath("$.content[0].questionType").value(response.content().get(0).questionType().toString()))
                .andExpect(jsonPath("$.nextCursor").value(response.nextCursor()))
                .andExpect(jsonPath("$.nextSubCursor").value(response.nextSubCursor()));
    }

    @Test
    @DisplayName("문제를 제출한다")
    void submit() throws Exception {
        // given
        QuestionMarkingRequest request = QuestionControllerFixture.markingRequest();
        given(questionCommandService.submit(anyLong(), anyString())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(post("/api/questions/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true));
    }

    @Test
    @DisplayName("1년간 문제 풀이 통계를 확인한다")
    @WithRestMockUser
    void getDailyWithinYear() throws Exception {
        // given
        Map<String, List<QuestionSubmitHistoryCountDto>> response = QuestionControllerFixture.withInYearResponse();
        given(questionSubmitHistoryQueryRepository.getDailyWithinYear(anyLong(), anyInt())).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/api/questions/histories/{year}", 2024));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$['2024-01']").isArray())
                .andExpect(jsonPath("$['2024-01'][0].date").value("2024-01-01"))
                .andExpect(jsonPath("$['2024-01'][0].count").value(2));
    }

    @Test
    @DisplayName("최근 30일간 풀이 통계를 확인한다")
    @WithRestMockUser
    void getLast30Days() throws Exception {
        // given
        List<QuestionSubmitHistoryCountDto> response = QuestionControllerFixture.last30DaysResponse();
        given(questionSubmitHistoryQueryRepository.getLast30Days(anyLong())).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/api/questions/histories/last-30-days"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2024-01-01"))
                .andExpect(jsonPath("$[0].count").value(2));
    }
}
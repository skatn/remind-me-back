package skatn.remindmeback.subject.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import skatn.remindmeback.common.controller.ControllerTest;
import skatn.remindmeback.common.fixture.SubjectControllerFixture;
import skatn.remindmeback.common.fixture.SubjectFixture;
import skatn.remindmeback.common.scroll.Scroll;
import skatn.remindmeback.common.security.WithRestMockUser;
import skatn.remindmeback.subject.contoller.dto.SubjectCreateRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectNotificationUpdateRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectScrollRequest;
import skatn.remindmeback.subject.contoller.dto.SubjectUpdateRequest;
import skatn.remindmeback.subject.dto.SubjectDto;
import skatn.remindmeback.subject.repository.SubjectQueryRepository;
import skatn.remindmeback.subject.repository.dto.SubjectListDto;
import skatn.remindmeback.subject.service.SubjectService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
class SubjectControllerTest extends ControllerTest {

    @MockBean
    SubjectService subjectService;
    @MockBean
    SubjectQueryRepository subjectQueryRepository;

    @Test
    @WithRestMockUser
    @DisplayName("문제집을 생성한다")
    void create() throws Exception {
        // given
        SubjectCreateRequest request = SubjectControllerFixture.createRequest();
        given(subjectService.create(anyLong(), anyString(), anyString())).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(post("/api/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.subjectId").value(1));
    }

    @Test
    @DisplayName("문제집을 조회한다")
    void getSubject() throws Exception {
        // given
        SubjectDto subjectDto = SubjectFixture.subjectDto();
        given(subjectService.findOne(anyLong())).willReturn(subjectDto);

        // when
        ResultActions result = mockMvc.perform(get("/api/subjects/{subjectId}", subjectDto.id()));

        // then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(subjectDto.id()))
                .andExpect(jsonPath("$.title").value(subjectDto.title()))
                .andExpect(jsonPath("$.color").value(subjectDto.color()));
    }

    @Test
    @DisplayName("문제집을 수정한다")
    void update() throws Exception {
        // given
        long subjectId = 1L;
        SubjectUpdateRequest request = SubjectControllerFixture.updateRequest();
        doNothing().when(subjectService).update(anyLong(), anyString(), anyString());

        // when
        ResultActions result = mockMvc.perform(patch("/api/subjects/{subjectId}", subjectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("문제집 알림 설정 상태를 조회한다")
    void getNotificationStatus() throws Exception {
        // given
        long subjectId = 1L;
        given(subjectService.getNotificationStatus(anyLong())).willReturn(false);

        // when
        ResultActions result = mockMvc.perform(get("/api/subjects/{subjectId}/notification", subjectId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.isEnable").value(false));
    }

    @Test
    @DisplayName("문제집 알림 설정을 변경한다")
    void updateNotification() throws Exception {
        // given
        long subjectId = 1L;
        SubjectNotificationUpdateRequest request = SubjectControllerFixture.notificationUpdateRequest();
        doNothing().when(subjectService).updateNotification(anyLong(), anyBoolean());

        // when
        ResultActions result = mockMvc.perform(patch("/api/subjects/{subjectId}/notification", subjectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("문제집을 삭제한다")
    void deleteSubject() throws Exception {
        // given
        long subjectId = 1L;
        doNothing().when(subjectService).delete(anyLong());

        // when
        ResultActions result = mockMvc.perform(delete("/api/subjects/{subjectId}", subjectId));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @WithRestMockUser
    @DisplayName("문제집 목록을 무한스크롤로 조회한다")
    void scrollSubjectList() throws Exception {
        // given
        SubjectScrollRequest request = SubjectControllerFixture.scrollRequest();
        Scroll<SubjectListDto> response = SubjectControllerFixture.scrollResponse();
        given(subjectQueryRepository.scrollSubjectList(anyLong(), any(), anyString()))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/api/subjects")
                .param("title", request.getTitle())
                .param("size", String.valueOf(request.getSize())));

        // then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.length()").value(response.content().size()))
                .andExpect(jsonPath("$.content[0].id").value(response.content().get(0).id()))
                .andExpect(jsonPath("$.content[0].title").value(response.content().get(0).title()))
                .andExpect(jsonPath("$.content[0].color").value(response.content().get(0).color()))
                .andExpect(jsonPath("$.content[0].questionCount").value(response.content().get(0).questionCount()))
                .andExpect(jsonPath("$.nextCursor").value(response.nextCursor()))
                .andExpect(jsonPath("$.nextSubCursor").value(response.nextSubCursor()));
    }
}
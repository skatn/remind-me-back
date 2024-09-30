package skatn.remindmeback.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import skatn.remindmeback.common.controller.ControllerTest;
import skatn.remindmeback.common.fixture.MemberControllerFixture;
import skatn.remindmeback.common.security.WithRestMockUser;
import skatn.remindmeback.member.controller.dto.MemberProfileUpdateRequest;
import skatn.remindmeback.member.service.MemberCommandService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest extends ControllerTest {

    @MockBean
    MemberCommandService memberCommandService;

    @Test
    @DisplayName("회원 프로필을 조회한다")
    @WithRestMockUser
    void getProfile() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(get("/api/members/me"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("member1"))
                .andExpect(jsonPath("$.name").value("member 1"));
    }

    @Test
    @DisplayName("회원 이름을 변경한다")
    @WithRestMockUser
    void changeName() throws Exception {
        // given
        MemberProfileUpdateRequest request = MemberControllerFixture.profileUpdateRequest();
        doNothing().when(memberCommandService).update(anyLong(), anyString());

        // when
        ResultActions result = mockMvc.perform(patch("/api/members/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("회원 계정을 삭제한다")
    @WithRestMockUser
    void deleteAccount() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(delete("/api/members/me"));

        // then
        result.andExpect(status().isNoContent());
    }



}
package com.cnu.spg.user.controller;

import com.cnu.spg.security.token.TokenProvider;
import com.cnu.spg.user.dto.requset.UserRegisterRequest;
import com.cnu.spg.user.exception.RoleNotFoundException;
import com.cnu.spg.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserApiController.class)
class UserApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;

    @MockBean
    TokenProvider tokenProvider;
    @MockBean
    UserDetailsService loginDetailService;
    @MockBean
    PasswordEncoder passwordEncoder;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void registerTest() throws Exception {
        // given
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .userName("kkh@gmail.com")
                .password("Abc123!")
                .matchingPassword("Abc123!")
                .name("kkh")
                .build();
        willReturn(1L)
                .given(userService).regiesterUser(any(UserRegisterRequest.class));

        // when
        String requestBodyStr = objectMapper.writeValueAsString(userRegisterRequest);
        ResultActions perform = mockMvc.perform(post("/api/v1/users")
                .content(requestBodyStr)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        String response = perform
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/users/1"))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("default role이 존재하지 않을 경우 에러 케이스")
    void registerFailDefaultRoleIsNotExistTest() throws Exception {
        // given
        willThrow(new RoleNotFoundException())
                .given(userService).regiesterUser(any(UserRegisterRequest.class));

        // when
        String requestBodyStr = objectMapper.writeValueAsString(new UserRegisterRequest("kkh@gmail.com", "Abc123!", "Abc123!", "kkh"));
        ResultActions perform = mockMvc.perform(post("/api/v1/users")
                .content(requestBodyStr)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        String errorMsg = perform
                .andExpect(status().isNotFound())
                .andExpect(header().doesNotExist("Location"))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(errorMsg)
                .isNotNull()
                .isNotEmpty();
    }
}
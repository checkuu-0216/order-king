package com.sparta.orderking.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.domain.auth.controller.AuthController;
import com.sparta.orderking.domain.auth.service.AuthService;
import com.sparta.orderking.domain.user.dto.request.LoginRequestDto;
import com.sparta.orderking.domain.user.dto.request.SignupRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signup_Success() throws Exception {
        SignupRequestDto requestDto = new SignupRequestDto("testUser", "aa@aa.com", "testPass123!");
        String bearerToken = "Bearertoken123";

        given(authService.signup(any(SignupRequestDto.class))).willReturn(bearerToken);

        ResultActions resultActions = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk()).andExpect(header().string(HttpHeaders.AUTHORIZATION, bearerToken));
    }

    @Test
    void login_Success() throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto("aa@aa.com", "testPass123!");
        String bearerToken = "Bearertoken123";

        given(authService.login(any(LoginRequestDto.class))).willReturn(bearerToken);

        ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, bearerToken));
    }
}

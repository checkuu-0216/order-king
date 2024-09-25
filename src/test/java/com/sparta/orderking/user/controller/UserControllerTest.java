package com.sparta.orderking.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.user.controller.UserController;
import com.sparta.orderking.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.orderking.domain.user.dto.response.UserResponseDto;
import com.sparta.orderking.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.sparta.orderking.store.CommonValue.TEST_AUTHUSER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void getUserProfile() throws Exception {
        UserResponseDto dto = new UserResponseDto();
        given(userService.getUser(TEST_AUTHUSER)).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(get("/api/users"));

        resultActions.andExpect(status().isOk());
    }


    @Test
    void deleteUser() throws Exception {
        DeleteUserRequestDto dto = new DeleteUserRequestDto();
        doNothing().when(userService).deleteUser(any(), any());

        ResultActions resultActions = mockMvc.perform(delete("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        resultActions.andExpect(status().isOk());
    }
}

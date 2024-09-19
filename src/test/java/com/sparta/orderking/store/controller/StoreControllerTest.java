package com.sparta.orderking.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.store.controller.StoreController;
import com.sparta.orderking.domain.store.dto.StoreDetailResponseDto;
import com.sparta.orderking.domain.store.dto.StoreResponseDto;
import com.sparta.orderking.domain.store.dto.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.orderking.store.CommonValue.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StoreController.class)
public class StoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StoreController(storeService))
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void 가게저장() throws Exception {
        given(storeService.saveStore(any(), any())).willReturn(TEST_STORERESPONSEDTO);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_STOREREQUESTDTO)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void 가게수정() throws Exception {
        Long storeId = 1L;
        given(storeService.updateStore(any(), anyLong(), any())).willReturn(TEST_STORERESPONSEDTO);

        ResultActions resultActions = mockMvc.perform(put("/api/stores/{storeId}", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_STOREREQUESTDTO)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void 가게단건조회() throws Exception {
        Long storeId = 1L;
        List<Menu> menuList = new ArrayList<>();
        StoreDetailResponseDto storeDetailResponseDto = new StoreDetailResponseDto(TEST_STORE, menuList);
        given(storeService.getDetailStore(anyLong())).willReturn(storeDetailResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/api/stores/{storeId}", storeId));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void 가게다건조회() throws Exception {
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();
        StoreSimpleRequestDto storeSimpleRequestDto = new StoreSimpleRequestDto("name");
        given(storeService.getStore(any())).willReturn(storeResponseDtoList);

        ResultActions resultActions = mockMvc.perform(get("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_STOREREQUESTDTO)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void 가게폐업() throws Exception {
        Long storeId = 1L;
        doNothing().when(storeService).closeStore(any(), anyLong());

        ResultActions resultActions = mockMvc.perform(put("/api/stores/{storeId}/close", storeId));

        resultActions.andExpect(status().isOk());
    }
}

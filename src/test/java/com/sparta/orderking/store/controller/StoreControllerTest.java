package com.sparta.orderking.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.menu.dto.MenuResponseDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import com.sparta.orderking.domain.store.controller.StoreController;
import com.sparta.orderking.domain.store.dto.request.StoreNotificationRequestDto;
import com.sparta.orderking.domain.store.dto.response.*;
import com.sparta.orderking.domain.store.dto.request.StoreSimpleRequestDto;
import com.sparta.orderking.domain.store.service.StoreService;
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

import java.util.ArrayList;
import java.util.List;

import static com.sparta.orderking.store.CommonValue.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@MockBean(JpaMetamodelMappingContext.class)
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

    /*@Test
    void 가게단건조회() throws Exception {
        Long storeId = 1L;
        List<Menu> menuList = new ArrayList<>();
        List<MenuResponseDto> menudtoList = new ArrayList<>();
        for(Menu m : menuList){
            MenuResponseDto dto = new MenuResponseDto(m.getMenuName(),
                    m.getMenuInfo(),
                    m.getMenuPrice(),
                    m.getMenuImg(),
                    m.getPossibleEnum());
            menudtoList.add(dto);
        }
        StoreDetailResponseDto storeDetailResponseDto = new StoreDetailResponseDto(TEST_STORE, menudtoList);
        given(storeService.getDetailStore(anyLong())).willReturn(storeDetailResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/api/stores/{storeId}", storeId));

        resultActions.andExpect(status().isOk());
    }*/

    @Test
    void 가게다건조회() throws Exception {
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();
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
    @Test
    void 광고시작() throws Exception {
        Long storeId =1L;
        doNothing().when(storeService).storeAdOn(any(),anyLong());

        ResultActions resultActions = mockMvc.perform(put("/api/stores/{storeId}/adon",storeId));

        resultActions.andExpect(status().isOk());
    }
    @Test
    void 광고끝() throws Exception {
        Long storeId =1L;
        doNothing().when(storeService).storeAdOff(any(),anyLong());

        ResultActions resultActions = mockMvc.perform(put("/api/stores/{storeId}/adoff",storeId));

        resultActions.andExpect(status().isOk());
    }
    @Test
    void checkDaily() throws Exception{
        List<StoreCheckDailyResponseDto> dto = new ArrayList<>();
        given(storeService.checkDailyMyStore(any())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(get("/api/stores/checkdaily"));

        resultActions.andExpect(status().isOk());
    }
    @Test
    void checkMonthly() throws Exception{
        List<StoreCheckMonthlyResponseDto> dto = new ArrayList<>();
        given(storeService.checkMonthlyMyStore(any())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(get("/api/stores/checkmonthly"));

        resultActions.andExpect(status().isOk());
    }
    @Test
    void 공지() throws Exception {
        Long storeId =1L;
        StoreNotificationResponseDto dto = new StoreNotificationResponseDto(TEST_STORE3);
        StoreNotificationRequestDto requestDto = new StoreNotificationRequestDto("notification");
        given(storeService.changeNotification(any(),anyLong(),any())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(put("/api/stores/{storeId}/notification",storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk());
    }
}

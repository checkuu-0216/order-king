package com.sparta.orderking.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.controller.MenuController;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
import com.sparta.orderking.domain.menu.dto.MenuUpdateRequestDto;
import com.sparta.orderking.domain.menu.entity.MenuCategoryEnum;
import com.sparta.orderking.domain.menu.entity.MenuPossibleEnum;
import com.sparta.orderking.domain.menu.service.MenuService;
import com.sparta.orderking.domain.user.entity.UserEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuController menuController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
    }
    
    //저장된 테스트코드
    //feat 선용좌
    @Test
    public void 메뉴_저장_컨트롤러() throws Exception {
        //given
        AuthUser authUser = new AuthUser(1L, UserEnum.OWNER);
        long storeId = 1L;
        MenuRequestDto menuRequestDto = new MenuRequestDto("a", "a", 10000, "a", MenuPossibleEnum.SALE, MenuCategoryEnum.KOREAN);
        willDoNothing().given(menuService).saveMenu(any(), anyLong(), any());
        String menuInfo = objectMapper.writeValueAsString(menuRequestDto);
        //when
        //then
        mockMvc.perform(post("/api/stores/{storeId}/menus", storeId)
                        .content(menuInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK, menu save complete."));
    }

    @Test
    public void 메뉴_수정_컨트롤러() throws Exception {
        //given
        AuthUser authUser = new AuthUser(1L, UserEnum.OWNER);
        long storeId = 1L;
        long menuId = 1L;
        MenuUpdateRequestDto menuRequestDto = new MenuUpdateRequestDto("a", "a", 10000, "a", MenuPossibleEnum.SALE, MenuCategoryEnum.KOREAN);
        willDoNothing().given(menuService).updateMenu(any(), anyLong(), anyLong(), any());
        String menuInfo = objectMapper.writeValueAsString(menuRequestDto);
        //when
        //then
        mockMvc.perform(put("/api/stores/{storeId}/menus/{menuId}", storeId, menuId)
                        .content(menuInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK, menu update complete."));
    }

    @Test
    public void 메뉴_삭제_컨트롤러() throws Exception {
        //given
        AuthUser authUser = new AuthUser(1L, UserEnum.OWNER);
        long storeId = 1L;
        long menuId = 1L;
        willDoNothing().given(menuService).updateMenu(any(), anyLong(), anyLong(), any());
        //when
        //then
        mockMvc.perform(delete("/api/stores/{storeId}/menus/{menuId}", storeId, menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK, menu delete complete."));
    }
}

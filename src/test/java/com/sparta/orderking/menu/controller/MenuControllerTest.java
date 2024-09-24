package com.sparta.orderking.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.domain.auth.dto.AuthUser;
import com.sparta.orderking.domain.menu.controller.MenuController;
import com.sparta.orderking.domain.menu.dto.MenuRequestDto;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(MenuControllerTest.class)
public class MenuControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuController menuController;

    @BeforeEach
    public void setUp () {
        this.mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
    }

    @Test
    public void 메뉴_저장_컨트롤러() {
        //given
        AuthUser authUser = new AuthUser(1L, UserEnum.OWNER);
        long storeId = 1L;
        MenuRequestDto menuRequestDto = new MenuRequestDto("a","a",10000,"a", MenuPossibleEnum.SALE, MenuCategoryEnum.KOREAN);
        willDoNothing().given(menuService).saveMenu(any(),anyLong(),any());
        //when
        //then
        mockMvc.perform(post("/api/stores/{storeId}/menus",storeId)
                .contentType)
    }
}

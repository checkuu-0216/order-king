package com.sparta.orderking.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.menu.entity.MenuCategoryEnum;
import com.sparta.orderking.domain.store.controller.SearchStoreController;
import com.sparta.orderking.domain.store.dto.response.StoreCategoryResponseDto;
import com.sparta.orderking.domain.store.dto.response.StoreSimpleResponseDto;
import com.sparta.orderking.domain.store.service.SearchStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(SearchStoreController.class)
public class SearchStoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchStoreService searchStoreService;

    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SearchStoreController(searchStoreService))
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void searchStores() throws Exception {
        String keyword = "1";
        List<StoreSimpleResponseDto> dtoList = new ArrayList<>();
        given(searchStoreService.searchStoreByNameOrMenu(anyString())).willReturn(dtoList);

        ResultActions resultActions = mockMvc.perform(get("/api/stores/search").param("keyword", keyword));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void searchCategory() throws Exception {
        MenuCategoryEnum menuCategoryEnum = MenuCategoryEnum.CHICKEN;
        List<StoreCategoryResponseDto> dtoList = new ArrayList<>();
        given(searchStoreService.searchStoresByCategory(any())).willReturn(dtoList);

        ResultActions resultActions = mockMvc.perform(get("/api/stores/category").param("menuCategoryEnum", String.valueOf(menuCategoryEnum)));

        resultActions.andExpect(status().isOk());
    }
}

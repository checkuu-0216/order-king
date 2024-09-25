package com.sparta.orderking.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.cart.controller.CartController;
import com.sparta.orderking.domain.cart.dto.CartRequestDto;
import com.sparta.orderking.domain.cart.dto.CartResponseDto;
import com.sparta.orderking.domain.cart.service.CartService;
import com.sparta.orderking.domain.menu.dto.MenuResponseDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CartController(cartService))
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void addMenu() throws Exception {
        Long storeId = 1L;
        List<MenuResponseDto> menuList = new ArrayList<>();
        CartResponseDto dto = new CartResponseDto(1L, menuList, 1L, LocalDateTime.now());
        CartRequestDto requestDto = new CartRequestDto();
        given(cartService.addMenu(anyLong(), anyLong(), any())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(post("/api/carts/store/{storeId}", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void getCart() throws Exception {
        List<MenuResponseDto> menuList = new ArrayList<>();
        CartResponseDto dto = new CartResponseDto(1L, menuList, 1L, LocalDateTime.now());

        given(cartService.getCart(anyLong())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(get("/api/carts"));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void clearCart() throws Exception {
        List<MenuResponseDto> menuList = new ArrayList<>();
        CartResponseDto dto = new CartResponseDto(1L, menuList, 1L, LocalDateTime.now());

        given(cartService.clearCart(anyLong())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(delete("/api/carts"));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void removeMenu() throws Exception {
        Long menuId = 1L;
        List<MenuResponseDto> menuList = new ArrayList<>();
        CartResponseDto dto = new CartResponseDto(1L, menuList, 1L, LocalDateTime.now());

        given(cartService.removeMenu(anyLong(), anyLong())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(delete("/api/carts/menu/{menuId}", menuId));

        resultActions.andExpect(status().isOk());
    }
}

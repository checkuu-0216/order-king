package com.sparta.orderking.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderking.config.AuthUserArgumentResolver;
import com.sparta.orderking.domain.order.controller.OrderController;
import com.sparta.orderking.domain.order.dto.OrderResponseDto;
import com.sparta.orderking.domain.order.dto.UpdateOrderStatusRequestDto;
import com.sparta.orderking.domain.order.service.OrderService;
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

import static com.sparta.orderking.store.CommonValue.TEST_ORDER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(OrderControllerTest.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUserArgumentResolver resolver;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService))
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void createOrder() throws Exception {
        Long storeId = 1L;
        doNothing().when(orderService).createOrder(anyLong(), anyLong());

        ResultActions resultActions = mockMvc.perform(post("/api/orders/{storeId}", storeId));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string("order success"));
    }

    @Test
    void updateOrderStatus() throws Exception {
        Long storeId = 1L;
        Long orderId = 1L;
        UpdateOrderStatusRequestDto dto = new UpdateOrderStatusRequestDto();
        doNothing().when(orderService).updateOrderStatus(anyLong(), anyLong(), anyLong(), any());

        ResultActions resultActions = mockMvc.perform(put("/api/orders/store/{storeId}/order/{orderId}", storeId, orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string("change order status success"));
    }

    @Test
    void getOrder() throws Exception {
        Long orderId = 1L;
        OrderResponseDto orderResponseDto = new OrderResponseDto(TEST_ORDER);
        given(orderService.getOrder(anyLong(), anyLong())).willReturn(orderResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/api/orders/{orderId}", orderId));

        resultActions.andExpect(status().isOk());
    }
}

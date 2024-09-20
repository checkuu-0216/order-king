package com.sparta.orderking.domain.order.dto;

import com.sparta.orderking.domain.order.enums.OrderStatus;
import lombok.Getter;

@Getter
public class UpdateOrderStatusRequestDto {
    private OrderStatus orderStatus;
}
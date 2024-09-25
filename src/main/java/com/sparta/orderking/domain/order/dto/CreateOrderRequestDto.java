package com.sparta.orderking.domain.order.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateOrderRequestDto {
    private List<Long> menuList = new ArrayList<>();
    private int price;
}
package com.sparta.orderking.domain.cart.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartRequestDto {
    private List<Long> menuList = new ArrayList<>();
}

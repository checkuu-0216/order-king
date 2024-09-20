package com.sparta.orderking.domain.order.dto;

import com.sparta.orderking.domain.menu.dto.MenuResponseDto;
import com.sparta.orderking.domain.order.entity.Order;
import com.sparta.orderking.domain.order.enums.OrderStatus;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {
    private int price;
    private OrderStatus orderStatus;
    private List<MenuResponseDto> menuList;

    public OrderResponseDto(Order order) {
        this.price = order.getPrice();
        this.orderStatus = order.getOrderStatus();
        this.menuList = order.getMenuList().stream()
                .map(orderMenu -> new MenuResponseDto(orderMenu.getMenu()))
                .collect(Collectors.toList());
    }
}

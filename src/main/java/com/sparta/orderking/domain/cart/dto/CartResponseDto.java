package com.sparta.orderking.domain.cart.dto;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.menu.dto.MenuResponseDto;
import com.sparta.orderking.domain.menu.entity.Menu;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CartResponseDto {
    private Long userId;
    private List<MenuResponseDto> menuList;
    private Long storeId;
    private LocalTime lastUpdated;

    public CartResponseDto(Cart cart) {
        this.userId = cart.getUser().getId();
        this.menuList = cart.getCartMenuList().stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
        this.storeId = cart.getStore().getId();
        this.lastUpdated = cart.getLastUpdated();
    }
}

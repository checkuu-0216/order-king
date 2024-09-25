package com.sparta.orderking.domain.cart.dto;

import com.sparta.orderking.domain.cart.entity.Cart;
import com.sparta.orderking.domain.menu.dto.MenuResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Getter
public class CartResponseDto {
    private Long userId;
    private List<MenuResponseDto> menuList;
    private Long storeId;
    private LocalDateTime lastUpdated;

    public CartResponseDto(Cart cart) {
        this.userId = cart.getUser().getId();
        this.menuList = cart.getCartMenuList().stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
        this.storeId = cart.getStore().getId();
        this.lastUpdated = cart.getLastUpdated();
    }
}

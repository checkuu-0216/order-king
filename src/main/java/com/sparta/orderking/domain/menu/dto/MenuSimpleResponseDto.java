package com.sparta.orderking.domain.menu.dto;

import lombok.Getter;

@Getter
public class MenuSimpleResponseDto {
    private final String menuName;

    public MenuSimpleResponseDto(String menuName) {
        this.menuName = menuName;
    }
}

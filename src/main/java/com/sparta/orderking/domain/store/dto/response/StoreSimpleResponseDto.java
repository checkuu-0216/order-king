package com.sparta.orderking.domain.store.dto.response;

import com.sparta.orderking.domain.menu.dto.MenuSimpleResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreSimpleResponseDto {
    private final String name;
    private final List<String> menus;

    public StoreSimpleResponseDto(String name, List<String> menus) {
        this.name = name;
        this.menus = menus;
    }
}

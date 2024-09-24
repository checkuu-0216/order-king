package com.sparta.orderking.domain.store.dto.response;

import com.sparta.orderking.domain.menu.dto.MenuSimpleResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreSimpleResponseDto {
    private final String name;


    public StoreSimpleResponseDto(String name) {
        this.name = name;

    }
}

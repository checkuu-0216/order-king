package com.sparta.orderking.domain.store.dto.response;

import lombok.Getter;

@Getter
public class StoreSimpleResponseDto {
    private final String name;


    public StoreSimpleResponseDto(String name) {
        this.name = name;

    }
}

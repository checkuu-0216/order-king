package com.sparta.orderking.domain.store.dto.response;

import lombok.Getter;

@Getter
public class StoreCategoryResponseDto {
    private final String name;

    public StoreCategoryResponseDto(String name) {
        this.name = name;
    }
}

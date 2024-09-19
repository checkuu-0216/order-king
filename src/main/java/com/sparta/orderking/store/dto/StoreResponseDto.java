package com.sparta.orderking.store.dto;

import com.sparta.orderking.store.entity.Store;
import lombok.Getter;

@Getter
public class StoreResponseDto {
    private final Long id;
    private final String name;

    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
    }
}

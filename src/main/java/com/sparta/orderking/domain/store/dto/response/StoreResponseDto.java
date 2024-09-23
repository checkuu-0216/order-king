package com.sparta.orderking.domain.store.dto.response;

import com.sparta.orderking.domain.store.entity.Store;
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

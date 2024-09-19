package com.sparta.orderking.domain.store.dto;

import com.sparta.orderking.domain.store.entity.StoreServiceEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {
    private String name;
    private String storeAddress;
    private String storeNumber;
    private int minPrice;
    private int openTime;
    private int closeTime;
    private StoreServiceEnum storeServiceEnum;
}
